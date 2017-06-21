package com.simplyti.cloud.kube.client.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class ServiceAccount extends KubernetesResource{
	
	private final List<ObjectReference> secrets;
	
	@JsonCreator
	public ServiceAccount(
			@JsonProperty("kind") String kind,
			@JsonProperty("apiVersion") String apiVersion,
			@JsonProperty("metadata") Metadata metadata,
			@JsonProperty("secrets") List<ObjectReference> secrets) {
		super(kind,apiVersion,metadata);
		this.secrets=secrets;
	}
	
}