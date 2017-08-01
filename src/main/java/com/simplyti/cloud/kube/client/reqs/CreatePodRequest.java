package com.simplyti.cloud.kube.client.reqs;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.simplyti.cloud.kube.client.domain.Container;
import com.simplyti.cloud.kube.client.domain.Metadata;
import com.simplyti.cloud.kube.client.domain.Pod;
import com.simplyti.cloud.kube.client.domain.PodSpec;
import com.simplyti.cloud.kube.client.domain.Probe;

import io.netty.handler.codec.http.HttpMethod;

public class CreatePodRequest extends KubernetesApiRequest {
	
	public CreatePodRequest(String namespace, String name, String image, Map<String, String> labels,Collection<String> command, Probe readinessProbe,Boolean mountServiceAccount) {
		super(HttpMethod.POST, "/api/v1/namespaces/"+namespace+"/pods",podResource(namespace,name,image,labels,command,readinessProbe,mountServiceAccount),Pod.class);
	}
	
	private static Pod podResource(String namespace, String name, String image,
			Map<String, String> labels, Collection<String> command, Probe readinessProbe,
			Boolean mountServiceAccount) {
		return new Pod(Pod.KIND, Pod.API, 
				Metadata.builder()
				.name(name)
				.namespace(namespace)
				.labels(labels)
				.build(),
				PodSpec.builder()
				.automountServiceAccountToken(mountServiceAccount)
				.containers(Collections.singleton(Container.builder()
						.name(name)
						.image(image)
						.readinessProbe(readinessProbe)
						.build()))
				.build(),
				null);
	}
}
