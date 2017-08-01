package com.simplyti.cloud.kube.client.domain;

import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class Endpoint extends KubernetesResource{
	
	public static final String KIND = "Endpoints";
	public static final String API = "v1";
	
	private final Collection<Subset> subsets;
	
	@JsonCreator
	public Endpoint(
			@JsonProperty("kind") String kind,
			@JsonProperty("apiVersion") String apiVersion,
			@JsonProperty("metadata") Metadata metadata,
			@JsonProperty("subsets") List<Subset> subsets) {
		super(kind,apiVersion,metadata);
		this.subsets=subsets;
	}

}