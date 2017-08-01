package com.simplyti.cloud.kube.client.domain;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

public class Service extends KubernetesResource{
	
	public static final String KIND = "Service";
	public static final String API = "v1";
	
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
