package com.simplyti.cloud.kube.client.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;


public class Pod extends KubernetesResource{
	
	@Getter
	private final PodStatus status;
	
	@Getter
	private final PodSpec spec;
	
	@JsonCreator
	public Pod(
			@JsonProperty("kind") String kind,
			@JsonProperty("apiVersion") String apiVersion,
			@JsonProperty("metadata") Metadata metadata,
			@JsonProperty("spec") PodSpec spec,
			@JsonProperty("status") PodStatus status) {
		super(kind,apiVersion,metadata);
		this.spec=spec;
		this.status=status;
	}

}
