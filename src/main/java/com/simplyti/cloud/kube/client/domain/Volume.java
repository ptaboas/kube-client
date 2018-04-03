package com.simplyti.cloud.kube.client.domain;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Getter;

@Getter
public abstract class Volume {
	
	private final String name;
	
	@JsonCreator
	public Volume(
			@JsonProperty("name") String name) {
		this.name=name;
	}

}
