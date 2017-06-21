package com.simplyti.cloud.kube.client.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NamespaceList extends KubernetesResourceList<Namespace>{

	@JsonCreator
	public NamespaceList(
			@JsonProperty("metadata") ListMetadata metadata, 
			@JsonProperty("items") List<Namespace> items) {
		super(metadata, items);
	}

	
}
