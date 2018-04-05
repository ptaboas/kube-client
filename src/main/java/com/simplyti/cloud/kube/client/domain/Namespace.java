package com.simplyti.cloud.kube.client.domain;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class Namespace extends KubernetesResource{
	
	private final NamespaceStatus status;

	@JsonCreator
	public Namespace(
			@JsonProperty("kind") String kind,
			@JsonProperty("apiVersion") String apiVersion,
			@JsonProperty("metadata") Metadata metadata,
			@JsonProperty("status") NamespaceStatus status){
		super(kind,apiVersion,metadata);
		this.status=status;
	}
	
}
