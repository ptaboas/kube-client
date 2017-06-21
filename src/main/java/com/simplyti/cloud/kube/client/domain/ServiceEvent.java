package com.simplyti.cloud.kube.client.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceEvent extends Event<Service> {

	@JsonCreator
	public ServiceEvent(
			@JsonProperty("type") EventType type, 
			@JsonProperty("object") Service object) {
		super(type, object);
	}

}
