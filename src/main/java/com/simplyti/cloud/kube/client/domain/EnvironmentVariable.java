package com.simplyti.cloud.kube.client.domain;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EnvironmentVariable {
	
	private final String name;
	
	@JsonCreator
	public EnvironmentVariable(
			@JsonProperty("name") String name){
		this.name=name;
	}

}
