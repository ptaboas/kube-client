package com.simplyti.cloud.kube.client;

import java.nio.channels.ClosedChannelException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.google.common.base.Joiner;
import com.simplyti.cloud.kube.client.domain.KubernetesResource;
import com.simplyti.cloud.kube.client.observe.Observable;
import com.simplyti.cloud.kube.client.reqs.KubernetesWatchApiRequest;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.pool.SimpleChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Log4J2LoggerFactory;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class InternalClient {
	
	public static final String SINGLE_RESPONSE_PROMISE_NAME = "singleResponsePromise";
	public static final AttributeKey<Promise<?>> SINGLE_RESPONSE_PROMISE = AttributeKey.valueOf(SINGLE_RESPONSE_PROMISE_NAME);
	
	public static final String OBSERVABLE_RESPONSE_NAME = "observableResponse";
	public static final AttributeKey<Observable<?>> OBSERVABLE_RESPONSE = AttributeKey.valueOf(OBSERVABLE_RESPONSE_NAME);
	
	private EventLoopGroup eventLoopGroup;
	private final SimpleChannelPool pool;

	public InternalClient(EventLoopGroup eventLoopGroup, Address server, boolean verbose, 
			Supplier<SslContext> sslContextProvider, Supplier<String> tokenProvider){
		InternalLoggerFactory.setDefaultFactory(Log4J2LoggerFactory.INSTANCE);
		this.eventLoopGroup=eventLoopGroup;
		 Bootstrap b = new Bootstrap().group(eventLoopGroup)
				.channel(channelClass())
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
				.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
				.remoteAddress(server.getHost(),server.getPort());
		this.pool = new SimpleChannelPool(b, new KubeChannelPoolHandler(Joiner.on(":").join(server.getHost(),server.getPort()),verbose,sslContextProvider,tokenProvider));
	}
	
	private Class<? extends Channel> channelClass() {
		return NioSocketChannel.class;
	}
	
	public <T> Future<T> sendRequest(Object request) {
		return sendRequest(newPromise(),request);
	}
	
	public <T> Future<T> sendRequest(Promise<T> promise, Object request) {
		return sendRequest(promise, request,false);
	}
	
	public <T> Future<T> sendRequest(Object request, boolean close) {
		return sendRequest(newPromise(), request, close);
	}
	
	public <T> Future<T> sendRequest(Promise<T> promise, Object request, boolean close) {
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
			channel.closeFuture().addListener(f->channel.pipeline().fireExceptionCaught(new ClosedChannelException()));
			channel.writeAndFlush(request).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
		}));
		return promise;
	}

	public <T extends KubernetesResource> Observable<T> observe(String index,Function<String,KubernetesWatchApiRequest> reqSupplier) {
		EventLoop executor = eventLoopGroup.next();
		Observable<T> observable = new Observable<>(executor,index);
		observe0(observable,reqSupplier);
		return observable;
	}
	
	private <T extends KubernetesResource> void observe0(Observable<T> observable,Function<String,KubernetesWatchApiRequest> reqSupplier) {
		pool.acquire().addListener(future->{
			if(future.isSuccess()){
				Channel channel = (Channel) future.get();
				observable.setChannel(channel,ob->this.observe0(ob,reqSupplier));
				channel.attr(OBSERVABLE_RESPONSE).set(observable);
				channel.writeAndFlush(reqSupplier.apply(observable.index()));
			}else{
				log.error("Error observing: {}",(Object)future.cause());
				observable.executor().schedule(()->observe0(observable,reqSupplier),3,TimeUnit.SECONDS);
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
