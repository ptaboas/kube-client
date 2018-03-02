package com.simplyti.cloud.kube.client.domain;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class KubernetesStatus extends KubernetesResource{

	private final String message;

	@JsonCreator
	public KubernetesStatus(
			@JsonProperty("kind") String kind,
			@JsonProperty("apiVersion") String apiVersion,
			@JsonProperty("metadata") Metadata metadata,
			@JsonProperty("message") String message) {
		super(kind,apiVersion,metadata);
		this.message=message;
	}

}
