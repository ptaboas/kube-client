package com.simplyti.cloud.kube.client.reqs;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.simplyti.cloud.kube.client.domain.Deployment;
import com.simplyti.cloud.kube.client.domain.Probe;

import io.netty.handler.codec.http.HttpMethod;

public class CreateDeploymentRequest extends KubernetesApiRequest {

	public CreateDeploymentRequest(String namespace, String name, String image, Map<String, String> labels,Collection<String> command, Probe readinessProbe) {
		super(HttpMethod.POST, "/apis/apps/v1beta1/namespaces/"+namespace+"/deployments",serviceResource(namespace,name,image,labels,command,readinessProbe),Deployment.class);
	}

	private static Object serviceResource(String namespace, String name, String image,
			Map<String, String> labels, Collection<String> command, Probe readinessProbe) {
		return ImmutableMap.builder()
			.put("kind", "Deployment")
			.put("apiVersion", "apps/v1beta1")
			.put("metadata", ImmutableMap.builder()
					.put("name", name)
					.put("labels", labels)
					.build())
			.put("spec", ImmutableMap.builder()
					.put("replicas",1)
					.put("template", ImmutableMap.builder()
							.put("replicas",1)
							.put("metadata", ImmutableMap.builder()
									.put("labels", labels)
									.build())
							.put("spec", ImmutableMap.builder()
									.put("automountServiceAccountToken", false)
									.put("containers",Collections.singleton(ImmutableMap.builder()
										.put("name", name)
										.put("image", image)
										.put("command",command)
										.put("readinessProbe",readinessProbe)
										.build()))
									.build())
							.build())
					.build())
			.build();
	}

}
