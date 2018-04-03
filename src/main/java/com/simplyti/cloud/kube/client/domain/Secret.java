package com.simplyti.cloud.kube.client.domain;

import java.util.Map;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class Secret extends KubernetesResource{
	
	private final Map<String, SecretData> data;
	private String type;

	@JsonCreator
	public Secret(
			@JsonProperty("kind") String kind,
			@JsonProperty("apiVersion") String apiVersion,
			@JsonProperty("metadata") Metadata metadata,
			@JsonProperty("data") Map<String,SecretData> data,
			@JsonProperty("type") String type) {
		super(kind,apiVersion,metadata);
		this.data=data;
		this.type=type;
	}
	
}