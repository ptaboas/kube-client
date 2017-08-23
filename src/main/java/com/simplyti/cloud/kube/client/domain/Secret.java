package com.simplyti.cloud.kube.client.domain;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class Secret extends KubernetesResource{
	
	private final Map<String, SecretData> data;

	@JsonCreator
	public Secret(
			@JsonProperty("kind") String kind,
			@JsonProperty("apiVersion") String apiVersion,
			@JsonProperty("metadata") Metadata metadata,
			@JsonProperty("data") Map<String,SecretData> data) {
		super(kind,apiVersion,metadata);
		this.data=data;
	}
	
}