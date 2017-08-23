package com.simplyti.cloud.kube.client;

import com.simplyti.cloud.kube.client.domain.KubernetesResource;

public interface NamespaceAwareKubeApi<T extends KubernetesResource, B extends CreationBuilder<T>,U extends Updater<T>,N extends NamespacedKubeApi<T,B,U>> {
	
	public N namespace(String namespace);

}
