package com.simplyti.cloud.kube.client.reqs;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.simplyti.cloud.kube.client.domain.Pod;
import com.simplyti.cloud.kube.client.domain.Probe;

import io.netty.handler.codec.http.HttpMethod;

public class CreatePodRequest extends KubernetesApiRequest {
	
	public CreatePodRequest(String namespace, String name, String image, Map<String, String> labels,Collection<String> command, Probe readinessProbe) {
		super(HttpMethod.POST, "/api/v1/namespaces/"+namespace+"/pods",serviceResource(namespace,name,image,labels,command,readinessProbe),Pod.class);
	}

	private static Object serviceResource(String namespace, String name, String image,
			Map<String, String> labels, Collection<String> command, Probe readinessProbe) {
		return ImmutableMap.builder()
			.put("kind", "Pod")
			.put("apiVersion", "v1")
			.put("metadata", ImmutableMap.builder()
					.put("name", name)
					.put("labels", labels)
					.build())
			.put("spec", ImmutableMap.builder()
					.put("automountServiceAccountToken", false)
					.put("containers",Collections.singleton(container(name, image,command,readinessProbe)))
					.build())
			.build();
	}

	private static Object container(String name, String image, Collection<String> command, Probe readinessProbe) {
		ImmutableMap.Builder<String, Object> builder = ImmutableMap.<String, Object>builder()
		.put("name", name)
		.put("image", image);
		
		if(command!=null){
			builder.put("command",command);
		}
		
		if(readinessProbe!=null){
			builder.put("readinessProbe",readinessProbe);
		}
		
		return builder.build();
	}
	
}
