package com.simplyti.cloud.kube.client.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class SecretList extends KubernetesResourceList<Secret>{

	@JsonCreator
	public SecretList(
			@JsonProperty("metadata") ListMetadata metadata, 
			@JsonProperty("items") List<Secret> items) {
		super(metadata, items);
	}
	
}
