package com.simplyti.cloud.kube.client.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PodEvent extends Event<Pod> {

	@JsonCreator
	public PodEvent(
			@JsonProperty("type") EventType type, 
			@JsonProperty("object") Pod object) {
		super(type, object);
	}

}
