package com.simplyti.cloud.kube.client;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

import com.jsoniter.spi.JsoniterSpi;
import com.jsoniter.spi.TypeLiteral;
import com.simplyti.cloud.kube.client.JsonPatch.PatchOperation;
import com.simplyti.cloud.kube.client.domain.EmptyDirVolume;
import com.simplyti.cloud.kube.client.domain.PodPhase;
import com.simplyti.cloud.kube.client.domain.Probe;
import com.simplyti.cloud.kube.client.domain.SecretData;
import com.simplyti.cloud.kube.client.domain.SecretVolume;
import com.simplyti.cloud.kube.client.domain.Volume;
import com.simplyti.cloud.kube.client.endpoints.DefaultEndpointsApi;
import com.simplyti.cloud.kube.client.endpoints.EndpointsApi;
import com.simplyti.cloud.kube.client.ingresses.DefaultIngressesApi;
import com.simplyti.cloud.kube.client.ingresses.IngressesApi;
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
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;

public class KubeClient {
	
	private static final TypeLiteral<String> STRING_TYPE = new TypeLiteral<String>() {};
	
	private final InternalClient internalClient;
	private final DefaultPodsApi pods;
	private final DefaultServicesApi services;
	private final DefaultEndpointsApi endpoints;
	private final DefaultNamespacesApi namespaces;
	private final DefaultSecretsApi secrets;
	private final DefaultServiceAccountsApi serviceaccounts;
	private final DefaultIngressesApi ingresses;
	
	@SuppressWarnings("unchecked")
	public KubeClient(EventLoopGroup eventLoopGroup, ApiServer server,boolean verbose, Supplier<String> tokenProvider){
		this.internalClient = new InternalClient(eventLoopGroup, server, verbose, tokenProvider);
		this.pods = new DefaultPodsApi(internalClient);
		this.services = new DefaultServicesApi(internalClient);
		this.endpoints = new DefaultEndpointsApi(internalClient);
		this.namespaces = new DefaultNamespacesApi(internalClient);
		this.secrets = new DefaultSecretsApi(internalClient);
		this.serviceaccounts = new DefaultServiceAccountsApi(internalClient);
		this.ingresses = new DefaultIngressesApi(internalClient);
		
		JsoniterSpi.registerTypeDecoder(LocalDateTime.class, iter->LocalDateTime.ofInstant(Instant.parse(iter.readString()), ZoneOffset.UTC));
		JsoniterSpi.registerTypeDecoder(PodPhase.class, iter->PodPhase.valueOf(iter.readString().toUpperCase()));
		JsoniterSpi.registerTypeDecoder(Probe.class, iter->{
			Map<?,?> map = iter.read(Map.class);
			int failureThreshold= (Integer) map.get("failureThreshold");
			int successThreshold= (Integer) map.get("successThreshold");
			int initialDelaySeconds = (Integer) map.get("initialDelaySeconds");
			int periodSeconds = (Integer) map.get("periodSeconds");
			if(map.containsKey("exec")) {
				Map<?,?> exec = (Map<?, ?>) map.get("exec");
				return Probe.exec((Collection<String>)exec.get("command"),failureThreshold,successThreshold,initialDelaySeconds,periodSeconds);
			}else if(map.containsKey("httpGet")) {
				Map<?,?> httpGet = (Map<?, ?>) map.get("httpGet");
				return Probe.http((String) httpGet.get("path"), (Integer) httpGet.get("port"), failureThreshold, successThreshold, initialDelaySeconds, periodSeconds);
			}else {
				Map<?,?> tcpSocket = (Map<?, ?>) map.get("tcpSocket");
				return Probe.tcp((Integer) tcpSocket.get("port"), failureThreshold, successThreshold, initialDelaySeconds, periodSeconds);
			}
		});
	
		JsoniterSpi.registerTypeEncoder(PatchOperation.class, (value,stream)->stream.writeVal(((PatchOperation)value).name().toLowerCase()));
		
		JsoniterSpi.registerTypeEncoder(SecretData.class, (value,stream)->stream.writeVal(Base64.getEncoder().encodeToString(SecretData.class.cast(value).getData())));
		JsoniterSpi.registerTypeDecoder(SecretData.class, iter->SecretData.of(Base64.getDecoder().decode(iter.readString().getBytes(CharsetUtil.UTF_8))));
	
		JsoniterSpi.registerTypeImplementation(Volume.class, EmptyDirVolume.class);
		JsoniterSpi.registerTypeImplementation(Volume.class, SecretVolume.class);
		
	}
	
	public Future<String> health() {
		return this.internalClient.sendRequest(new GetHealthRequest(),STRING_TYPE);
	}
	
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
	
	public IngressesApi ingresses() {
		return ingresses;
	}

	public void close() {
		this.internalClient.close();
	}

	public NamespacedClient namespace(String namespace) {
		return new NamespacedClient(internalClient,namespace,services,pods,endpoints,secrets,serviceaccounts,ingresses);
	}
	
	public static KubeClientBuilder builder() {
		return new KubeClientBuilder();
	}

}
