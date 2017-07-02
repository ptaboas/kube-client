package com.simplyti.cloud.kube.client.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PodList extends KubernetesResourceList<Pod>{

	@JsonCreator
	public PodList(
			@JsonProperty("metadata") ListMetadata metadata, 
			@JsonProperty("items") List<Pod> items) {
		super(metadata, items);
	}

}
