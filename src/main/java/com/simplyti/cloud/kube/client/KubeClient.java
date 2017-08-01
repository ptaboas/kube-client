package com.simplyti.cloud.kube.client;


import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import com.google.common.base.Joiner;
import com.simplyti.cloud.kube.client.domain.Endpoint;
import com.simplyti.cloud.kube.client.domain.EndpointList;
import com.simplyti.cloud.kube.client.domain.KubernetesResource;
import com.simplyti.cloud.kube.client.domain.Namespace;
import com.simplyti.cloud.kube.client.domain.NamespaceList;
import com.simplyti.cloud.kube.client.domain.Pod;
import com.simplyti.cloud.kube.client.domain.PodList;
import com.simplyti.cloud.kube.client.domain.Probe;
import com.simplyti.cloud.kube.client.domain.Secret;
import com.simplyti.cloud.kube.client.domain.SecretList;
import com.simplyti.cloud.kube.client.domain.Service;
import com.simplyti.cloud.kube.client.domain.ServiceAccount;
import com.simplyti.cloud.kube.client.domain.ServiceList;
import com.simplyti.cloud.kube.client.domain.ServicePort;
import com.simplyti.cloud.kube.client.observe.Observable;
import com.simplyti.cloud.kube.client.reqs.CreateEndpointRequest;
import com.simplyti.cloud.kube.client.reqs.CreateNamespaceRequest;
import com.simplyti.cloud.kube.client.reqs.CreatePodRequest;
import com.simplyti.cloud.kube.client.reqs.CreateSecretRequest;
import com.simplyti.cloud.kube.client.reqs.CreateServiceRequest;
import com.simplyti.cloud.kube.client.reqs.DeleteEndpointRequest;
import com.simplyti.cloud.kube.client.reqs.DeleteNamespaceRequest;
import com.simplyti.cloud.kube.client.reqs.DeletePodRequest;
import com.simplyti.cloud.kube.client.reqs.DeleteServiceRequest;
import com.simplyti.cloud.kube.client.reqs.GetEndpointEventsRequest;
import com.simplyti.cloud.kube.client.reqs.GetEndpointRequest;
import com.simplyti.cloud.kube.client.reqs.GetHealthRequest;
import com.simplyti.cloud.kube.client.reqs.GetNamespacesRequest;
import com.simplyti.cloud.kube.client.reqs.GetPodEventsRequest;
import com.simplyti.cloud.kube.client.reqs.GetPodRequest;
import com.simplyti.cloud.kube.client.reqs.GetPodsRequest;
import com.simplyti.cloud.kube.client.reqs.GetSecretRequest;
import com.simplyti.cloud.kube.client.reqs.GetSecretsRequest;
import com.simplyti.cloud.kube.client.reqs.GetServiceAccount;
import com.simplyti.cloud.kube.client.reqs.GetServiceEventsRequest;
import com.simplyti.cloud.kube.client.reqs.GetServiceRequest;
import com.simplyti.cloud.kube.client.reqs.GetServicesRequest;
import com.simplyti.cloud.kube.client.reqs.KubernetesApiRequest;
import com.simplyti.cloud.kube.client.reqs.KubernetesCommandExec;
import com.simplyti.cloud.kube.client.reqs.UpdateServiceRequest;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
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

public class KubeClient {
	
	public static final String SINGLE_RESPONSE_PROMISE_NAME = "singleResponsePromise";
	public static final AttributeKey<Promise<?>> SINGLE_RESPONSE_PROMISE = AttributeKey.valueOf(SINGLE_RESPONSE_PROMISE_NAME);
	
	public static final String OBSERVABLE_RESPONSE_NAME = "observableResponse";
	public static final AttributeKey<Observable<?>> OBSERVABLE_RESPONSE = AttributeKey.valueOf(OBSERVABLE_RESPONSE_NAME);

	public static KubeClientBuilder builder() {
		return new KubeClientBuilder();
	}

	private final SimpleChannelPool pool;
	private final EventLoopGroup eventLoopGroup;
	
	public KubeClient(EventLoopGroup eventLoopGroup, Address server,boolean verbose, Supplier<SslContext> sslContextProvider,
			Supplier<String> tokenProvider){
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
	
	public Future<String> health() {
		return sendRequest(new GetHealthRequest());
	}

	public Future<ServiceList> getServices() {
		return sendRequest(new GetServicesRequest());
	}
	
	public Future<ServiceList> getServices(String namespace) {
		return sendRequest(new GetServicesRequest(namespace));
	}
	
	public Future<Service> getservice(String namespace, String name) {
		return sendRequest(new GetServiceRequest(namespace,name));
	}

	public Future<EndpointList> getEndpoints() {
		return sendRequest(new GetEndpointRequest());
	}

	public Future<Service> createService(String namespace, String name, Collection<ServicePort> ports) {
		return createService(namespace,name,ports,null);
	}
	
	public Future<Service> createService(String namespace, String name, Collection<ServicePort> ports,Map<String, String> labelSelector) {
		return createService(namespace,name,ports,labelSelector,null);
	}
	
	public Future<Service> createService(String namespace, String name, Collection<ServicePort> ports,Map<String, String> labelSelector,Map<String, String> annotations) {
		return sendRequest(new CreateServiceRequest(namespace,name,ports,labelSelector,annotations));
	}
	
	public Future<Service> updateService(Service service) {
		return sendRequest(new UpdateServiceRequest(service));
	}
	
	public Future<Void> deleteService(String namespace, String name) {
		return sendRequest(new DeleteServiceRequest(namespace,name));
	}
	
	public Future<Pod> createPod(String namespace, String name, String image, Boolean mountServiceAccount) {
		return createPod(namespace,name,image,null,null,null,mountServiceAccount);
	}
	
	public Future<Pod> createPod(String namespace, String name, String image, Probe readinessProbe, Boolean mountServiceAccount) {
		return createPod(namespace,name,image,null,null,readinessProbe,mountServiceAccount);
	}
	
	public Future<Pod> createPod(String namespace, String name, String image, Map<String, String> labels, Collection<String> command, Probe readinessProbe,
			Boolean mountServiceAccount) {
		return sendRequest(new CreatePodRequest(namespace,name,image,labels,command,readinessProbe,mountServiceAccount));
	}
	
	public Future<Void> deletePod(String namespace, String name) {
		return sendRequest(new DeletePodRequest(namespace,name));
	}
	
	public Future<Pod> getPod(String namespace, String name) {
		return sendRequest(new GetPodRequest(namespace,name));
	}
	
	public Future<PodList> getpods(String namespace) {
		return sendRequest(new GetPodsRequest(namespace));
	}
	
	public Future<PodList> getPods() {
		return sendRequest(new GetPodsRequest());
	}

	public Future<Namespace> createNamespace(String name) {
		return sendRequest(new CreateNamespaceRequest(name));
	}

	public Future<Void> deleteNamespace(String name) {
		return sendRequest(new DeleteNamespaceRequest(name));
	}

	public Future<NamespaceList> getNamespaces() {
		return sendRequest(new GetNamespacesRequest());
	}
	
	public Future<SecretList> getSecrets() {
		return sendRequest(new GetSecretsRequest());
	}
	
	public Future<Secret> getSecret(String namespace, String name) {
		return sendRequest(new GetSecretRequest(namespace,name));
	}
	
	public Future<ServiceAccount> getServiceAccount(String namespace, String name) {
		return sendRequest(new GetServiceAccount(namespace,name));
	}
	
	public Future<Void> deleteEndpoint(String namespace, String name) {
		return sendRequest(new DeleteEndpointRequest(namespace,name));
	}
	
	public Future<Endpoint> createEndpoint(String namespace, String name, Set<String> addresses, Set<Integer> ports) {
		return sendRequest(new CreateEndpointRequest(namespace,name,addresses,ports));
	}
	
	public Future<Secret> createSecret(String namespace, String name, Map<String, String> data) {
		return sendRequest(new CreateSecretRequest(namespace,name,data));
	}

	private <T> Future<T> sendRequest(KubernetesApiRequest apiRequest) {
		Promise<T> promise = eventLoopGroup.next().newPromise();
		pool.acquire().addListener(future->{
			 Channel channel = (Channel) future.get();
			 promise.addListener(done->pool.release(channel));
			 channel.attr(SINGLE_RESPONSE_PROMISE).set(promise);
			 channel.writeAndFlush(apiRequest);
		});
		return promise;
	}
	
	public Observable<Service> observeServices(String index) {
		return observeServices(index,newIndex->new GetServiceEventsRequest(newIndex));
	}
	
	public Observable<Endpoint> observeEndpoints(String index) {
		return observeServices(index,newIndex->new GetEndpointEventsRequest(newIndex));
	}
	
	public Observable<Pod> observePods(String index) {
		return observeServices(index,newIndex->new GetPodEventsRequest(newIndex));
	}
	
	public <T extends KubernetesResource> Observable<T> observeServices(String index,Function<String,KubernetesApiRequest> reqSupplier) {
		EventLoop executor = eventLoopGroup.next();
		Observable<T> observable = new Observable<>(executor,index);
		observeServices0(observable,reqSupplier);
		return observable;
	}

	private <T extends KubernetesResource> void observeServices0(Observable<T> observable,Function<String,KubernetesApiRequest> reqSupplier) {
		pool.acquire().addListener(future->{
			 Channel channel = (Channel) future.get();
			 observable.setChannel(channel,ob->this.observeServices0(ob,reqSupplier));
			 channel.attr(OBSERVABLE_RESPONSE).set(observable);
			 channel.writeAndFlush(reqSupplier.apply(observable.index()));
		});
	}


	public Future<byte[]> executeCommand(String namespace, String name, String command) {
		Promise<byte[]> promise = eventLoopGroup.next().newPromise();
		pool.acquire().addListener(future->{
			 Channel channel = (Channel) future.get();
			 promise.addListener(done->channel.close());
			 channel.attr(SINGLE_RESPONSE_PROMISE).set(promise);
			 channel.writeAndFlush(new KubernetesCommandExec(namespace,name,command));
		});
		return promise;
	}

	public void close() {
		eventLoopGroup.shutdownGracefully();
	}

}
