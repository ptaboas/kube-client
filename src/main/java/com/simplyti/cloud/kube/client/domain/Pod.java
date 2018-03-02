package com.simplyti.cloud.kube.client.domain;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class Pod extends KubernetesResource{
	
	private final PodStatus status;
	
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
