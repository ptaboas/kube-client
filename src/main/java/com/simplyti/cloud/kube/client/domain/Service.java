package com.simplyti.cloud.kube.client.domain;


import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Getter;

public class Service extends KubernetesResource{
	
	@Getter
	private final ServiceSpec spec;

	@JsonCreator
	public Service(
			@JsonProperty("kind") String kind,
			@JsonProperty("apiVersion") String apiVersion,
			@JsonProperty("metadata") Metadata metadata,
			@JsonProperty("spec") ServiceSpec spec) {
		super(kind,apiVersion,metadata);
		this.spec=spec;
	}
	
}
