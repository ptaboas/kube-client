package com.simplyti.cloud.kube.client.domain;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class Ingress extends KubernetesResource{
	
	private final IngressSpec spec;

	@JsonCreator
	public Ingress(
			@JsonProperty("kind") String kind,
			@JsonProperty("apiVersion") String apiVersion,
			@JsonProperty("metadata") Metadata metadata,
			@JsonProperty("spec") IngressSpec spec) {
		super(kind,apiVersion,metadata);
		this.spec=spec;
	}

}
