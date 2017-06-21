package com.simplyti.cloud.kube.client.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceList extends KubernetesResourceList<Service>{

	@JsonCreator
	public ServiceList(
			@JsonProperty("metadata") ListMetadata metadata, 
			@JsonProperty("items") List<Service> items) {
		super(metadata, items);
	}

}
