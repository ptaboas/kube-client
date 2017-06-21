package com.simplyti.cloud.kube.client.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EndpointEvent extends Event<Endpoint> {

	@JsonCreator
	public EndpointEvent(
			@JsonProperty("type") EventType type, 
			@JsonProperty("object") Endpoint object) {
		super(type, object);
	}

}
