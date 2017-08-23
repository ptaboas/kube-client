package com.simplyti.cloud.kube.client.pods;

import com.simplyti.cloud.kube.client.KubeApi;
import com.simplyti.cloud.kube.client.NamespaceAwareKubeApi;
import com.simplyti.cloud.kube.client.domain.Pod;

public interface PodsApi extends KubeApi<Pod>, NamespaceAwareKubeApi<Pod,PodCreationBuilder,PodUpdater,NamespacedPodsApi>{

}
