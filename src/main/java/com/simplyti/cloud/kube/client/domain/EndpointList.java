package com.simplyti.cloud.kube.client.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EndpointList extends KubernetesResourceList<Endpoint>{

	@JsonCreator
	public EndpointList(
			@JsonProperty("metadata") ListMetadata metadata, 
			@JsonProperty("items") List<Endpoint> items) {
		super(metadata, items);
	}

}
