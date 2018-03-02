package com.simplyti.cloud.kube.client.domain;


import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class Event<T extends KubernetesResource> {
	
	private final EventType type;
	private final T object;
	
	@JsonCreator
	public Event(
			@JsonProperty("type") EventType type,
			@JsonProperty("object") T object) {
		this.type=type;
		this.object=object;
	}

}
