package com.simplyti.cloud.kube.client.domain;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContainerResources {
	
	private final String memory;
	private final String cpu;

	@JsonCreator
	public ContainerResources(
			@JsonProperty("memory") String memory,
			@JsonProperty("cpu ") String cpu){
		this.memory=memory;
		this.cpu=cpu;
	}

}
