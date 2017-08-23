package com.simplyti.cloud.kube.client;

import java.util.function.Supplier;

import com.simplyti.cloud.kube.client.endpoints.DefaultEndpointsApi;
import com.simplyti.cloud.kube.client.endpoints.EndpointsApi;
import com.simplyti.cloud.kube.client.namespaces.DefaultNamespacesApi;
import com.simplyti.cloud.kube.client.namespaces.NamespacesApi;
import com.simplyti.cloud.kube.client.pods.DefaultPodsApi;
import com.simplyti.cloud.kube.client.pods.PodsApi;
import com.simplyti.cloud.kube.client.reqs.GetHealthRequest;
import com.simplyti.cloud.kube.client.secrets.DefaultSecretsApi;
import com.simplyti.cloud.kube.client.secrets.SecretsApi;
import com.simplyti.cloud.kube.client.serviceaccounts.DefaultServiceAccountsApi;
import com.simplyti.cloud.kube.client.serviceaccounts.ServiceAccountsApi;
import com.simplyti.cloud.kube.client.services.DefaultServicesApi;
import com.simplyti.cloud.kube.client.services.ServicesApi;

import io.netty.channel.EventLoopGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.util.concurrent.Future;

public class KubeClient {
	
	public static KubeClientBuilder builder() {
		return new KubeClientBuilder();
	}

	private final InternalClient internalClient;
	private final DefaultPodsApi pods;
	private final DefaultServicesApi services;
	private final DefaultEndpointsApi endpoints;
	private final DefaultNamespacesApi namespaces;
	private final DefaultSecretsApi secrets;
	private final DefaultServiceAccountsApi serviceaccounts;
	
	public KubeClient(EventLoopGroup eventLoopGroup, Address server,boolean verbose, 
			Supplier<SslContext> sslContextProvider, Supplier<String> tokenProvider){
		this.internalClient = new InternalClient(eventLoopGroup, server, verbose, sslContextProvider, tokenProvider);
		this.pods = new DefaultPodsApi(internalClient);
		this.services = new DefaultServicesApi(internalClient);
		this.endpoints = new DefaultEndpointsApi(internalClient);
		this.namespaces = new DefaultNamespacesApi(internalClient);
		this.secrets = new DefaultSecretsApi(internalClient);
		this.serviceaccounts = new DefaultServiceAccountsApi(internalClient);
	}
	
	public Future<String> health() {
		return this.internalClient.sendRequest(new GetHealthRequest());
	}

//	public Future<Service> updateService(Service service) {
//		return this.internalClient.sendRequest(new UpdateServiceRequest(service));
//	}
	
	public PodsApi pods() {
		return pods;
	}
	
	public ServicesApi services() {
		return services;
	}
	
	public EndpointsApi endpoints() {
		return endpoints;
	}
	
	public NamespacesApi namespaces() {
		return namespaces;
	}
	
	public SecretsApi secrets() {
		return secrets;
	}
	
	public ServiceAccountsApi serviceaccounts() {
		return serviceaccounts;
	}

	public void close() {
		this.internalClient.close();
	}

	public NamespacedClient namespace(String namespace) {
		return new NamespacedClient(internalClient,namespace,services,pods,endpoints,secrets,serviceaccounts);
	}

}
