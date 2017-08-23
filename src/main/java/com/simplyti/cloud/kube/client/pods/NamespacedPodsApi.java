package com.simplyti.cloud.kube.client.pods;

import com.simplyti.cloud.kube.client.NamespacedKubeApi;
import com.simplyti.cloud.kube.client.domain.Pod;

import io.netty.util.concurrent.Future;

public interface NamespacedPodsApi extends NamespacedKubeApi<Pod, PodCreationBuilder,PodUpdater>{

	Future<byte[]> execute(String name, String command);

	Future<byte[]> execute(String name, String container, String command);

}
