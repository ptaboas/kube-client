package com.simplyti.cloud.kube.client.pods;

import com.simplyti.cloud.kube.client.AbstractAllKubeApi;
import com.simplyti.cloud.kube.client.InternalClient;
import com.simplyti.cloud.kube.client.domain.Pod;

public class DefaultPodsApi extends AbstractAllKubeApi<Pod,PodCreationBuilder,PodUpdater,NamespacedPodsApi> implements PodsApi {

	private static final String API = "v1";
	
	public DefaultPodsApi(InternalClient client) {
		super(client,API,"pods");
	}

	@Override
	public PodCreationBuilder builder(String namespace) {
		return new PodCreationBuilder(client,API,namespace);
	}

	@Override
	public NamespacedPodsApi namespace(String namespace) {
		return new DefaultNamespacedPodsApi(client,namespace,this);
	}

	@Override
	public PodUpdater updateBuilder(String namespace, String name) {
		return new PodUpdater(client,API, namespace,name,resourceName,resourceClass,this);
	}
	
}
