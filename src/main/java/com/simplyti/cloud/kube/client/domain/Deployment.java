package com.simplyti.cloud.kube.client.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

public class Deployment extends KubernetesResource{
	
	@Getter
	private final DeploymentSpec spec;

	@JsonCreator
	public Deployment(
			@JsonProperty("kind") String kind,
			@JsonProperty("apiVersion") String apiVersion,
			@JsonProperty("metadata") Metadata metadata,
			@JsonProperty("spec") DeploymentSpec spec) {
		super(kind,apiVersion,metadata);
		this.spec=spec;
	}
	
}
