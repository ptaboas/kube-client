package com.simplyti.cloud.kube.client.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class KubernetesResource {

	private final String kind;
	private final String apiVersion;
	private final Metadata metadata;
}
