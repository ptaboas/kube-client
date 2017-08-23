package com.simplyti.cloud.kube.client.pods;

import com.fasterxml.jackson.core.type.TypeReference;
import com.simplyti.cloud.kube.client.AbstractUpdater;
import com.simplyti.cloud.kube.client.InternalClient;
import com.simplyti.cloud.kube.client.ResourceSupplier;
import com.simplyti.cloud.kube.client.domain.Pod;

public class PodUpdater extends AbstractUpdater<Pod,PodUpdater>{

	public PodUpdater(InternalClient client, String namespace, String name, String resourceName,
			TypeReference<Pod> resourceClass,ResourceSupplier<Pod, ?,?> supplier) {
		super(client, namespace, name, resourceName, resourceClass,supplier);
	}

}
