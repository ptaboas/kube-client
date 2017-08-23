package com.simplyti.cloud.kube.client.pods;

import java.util.stream.Collectors;

import com.google.common.collect.Iterables;
import com.simplyti.cloud.kube.client.AbstractNamespacedKubeApi;
import com.simplyti.cloud.kube.client.InternalClient;
import com.simplyti.cloud.kube.client.ResourceSupplier;
import com.simplyti.cloud.kube.client.domain.Container;
import com.simplyti.cloud.kube.client.domain.Pod;
import com.simplyti.cloud.kube.client.reqs.KubernetesCommandExec;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;

public class DefaultNamespacedPodsApi extends AbstractNamespacedKubeApi<Pod, PodCreationBuilder,PodUpdater> implements NamespacedPodsApi {

	private final InternalClient client;

	public DefaultNamespacedPodsApi(InternalClient client, String namespace, ResourceSupplier<Pod, PodCreationBuilder, PodUpdater> resourceSupplier) {
		super(namespace, resourceSupplier);
		this.client=client;
	}

	@Override
	public Future<byte[]> execute(String name, String command) {
		Promise<byte[]> promise = client.newPromise();
		get(name).addListener(futurePod->{
			if(futurePod.isSuccess()){
				Pod pod = (Pod) futurePod.getNow();
				if(pod.getSpec().getContainers().size()==1){
					String container = Iterables.getFirst(pod.getSpec().getContainers(), null).getName();
					client.sendRequest(promise,new KubernetesCommandExec(namespace,name,container,command),true);
				}else{
					promise.setFailure(new IllegalArgumentException("Must specify container name: "+pod.getSpec().getContainers().stream().map(Container::getName).collect(Collectors.toSet())));
				}
			}else{
				promise.setFailure(futurePod.cause());
			}
		});
		return promise;
	}

	@Override
	public Future<byte[]> execute(String name, String container, String command) {
		return client.sendRequest(new KubernetesCommandExec(namespace,name,container,command),true);
	}

}
