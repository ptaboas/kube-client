package com.simplyti.cloud.kube.client;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.google.common.base.Joiner;
import com.jsoniter.spi.TypeLiteral;
import com.simplyti.cloud.kube.client.domain.Event;
import com.simplyti.cloud.kube.client.domain.KubernetesResource;
import com.simplyti.cloud.kube.client.observe.Observable;
import com.simplyti.cloud.kube.client.reqs.KubernetesApiRequest;
import com.simplyti.cloud.kube.client.reqs.KubernetesWatchApiRequest;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.kqueue.KQueueSocketChannel;
import io.netty.channel.pool.SimpleChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public class InternalClient {
	
	private final InternalLogger log = InternalLoggerFactory.getInstance(getClass());
	
	public static final AttributeKey<TypeLiteral<?>> RESPONSE_CLASS = AttributeKey.valueOf("responseClass");
	
	public static final String SINGLE_RESPONSE_PROMISE_NAME = "singleResponsePromise";
	public static final AttributeKey<Promise<?>> SINGLE_RESPONSE_PROMISE = AttributeKey.valueOf(SINGLE_RESPONSE_PROMISE_NAME);
	
	public static final String OBSERVABLE_RESPONSE_NAME = "observableResponse";
	public static final AttributeKey<Observable<?>> OBSERVABLE_RESPONSE = AttributeKey.valueOf(OBSERVABLE_RESPONSE_NAME);
	
	private EventLoopGroup eventLoopGroup;
	private final SimpleChannelPool pool;

	public InternalClient(EventLoopGroup eventLoopGroup, ApiServer server, boolean verbose, Supplier<String> tokenProvider){
		this.eventLoopGroup=eventLoopGroup;
		 Bootstrap b = new Bootstrap().group(eventLoopGroup)
				.channel(channelClass())
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
				.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
				.remoteAddress(server.getHost(),server.getPort());
		this.pool = new SimpleChannelPool(b, new KubeChannelPoolHandler(Joiner.on(":").join(server.getHost(),server.getPort()),verbose,server.getSslContextProvider(),tokenProvider));
	}
	
	private Class<? extends Channel> channelClass() {
		return Match(eventLoopGroup).of(
				Case($(instanceOf(EpollEventLoopGroup.class)), EpollSocketChannel.class),
				Case($(instanceOf(KQueueEventLoopGroup.class)), KQueueSocketChannel.class),
				Case($(), NioSocketChannel.class));
	}
	
	public <T> Future<T> sendRequest(KubernetesApiRequest request, TypeLiteral<T> responseClass) {
		return sendRequest(newPromise(),request,responseClass);
	}
	
	public <T> Future<T> sendRequest(Promise<T> promise, KubernetesApiRequest request, TypeLiteral<T> responseClass) {
		return sendRequest(promise, request, responseClass, false);
	}
	
	public <T> Future<T> sendRequest(KubernetesApiRequest request, TypeLiteral<T> responseClass, boolean close) {
		return sendRequest(newPromise(), request, responseClass, close);
	}
	
	public <T> Future<T> sendRequest(Promise<T> promise, KubernetesApiRequest request, TypeLiteral<T> responseClass, boolean close) {
		pool.acquire().addListener(future->
		ifSuccess(future,promise,channel->{
			promise.addListener(done->{
				if(close){
					channel.close();
				}else{
					pool.release(channel);
				}
			});
			channel.attr(SINGLE_RESPONSE_PROMISE).set(promise);
			channel.attr(RESPONSE_CLASS).set(responseClass);
			channel.writeAndFlush(request).addListener(writeFuture->{
				if(!writeFuture.isSuccess()){
					promise.tryFailure(writeFuture.cause());
				}
			});
		}));
		return promise;
	}

	public <T extends KubernetesResource> Observable<T> observe(String index,Function<String,KubernetesWatchApiRequest> reqSupplier, TypeLiteral<Event<T>> responseClass) {
		EventLoop executor = eventLoopGroup.next();
		Observable<T> observable = new Observable<>(executor,index);
		observe0(observable,reqSupplier,responseClass);
		return observable;
	}
	
	private <T extends KubernetesResource> void observe0(Observable<T> observable,Function<String,KubernetesWatchApiRequest> reqSupplier, TypeLiteral<Event<T>> responseClass) {
		pool.acquire().addListener(future->{
			if(future.isSuccess()){
				Channel channel = (Channel) future.get();
				channel.attr(RESPONSE_CLASS).set(responseClass);
				channel.attr(OBSERVABLE_RESPONSE).set(observable);
				observable.setChannel(channel,ob->this.observe0(ob,reqSupplier,responseClass));
				channel.writeAndFlush(reqSupplier.apply(observable.index()));
			}else{
				log.error("Error observing: {}",(Object)future.cause());
				observable.executor().schedule(()->observe0(observable,reqSupplier,responseClass),3,TimeUnit.SECONDS);
			}
		});
	}
	
	private void ifSuccess(Future<? super Channel> future, Promise<?> promise, Consumer<Channel> consumer) {
		if(future.isSuccess()){
			Channel channel = (Channel) future.getNow();
			consumer.accept(channel);
		}else{
			promise.setFailure(future.cause());
		}
	}
	
	public void close() {
		this.eventLoopGroup.shutdownGracefully();
	}

	public <T> Promise<T> newPromise() {
		return eventLoopGroup.next().newPromise();
	}

	public EventLoop nextExecutor() {
		return this.eventLoopGroup.next();
	}

}
