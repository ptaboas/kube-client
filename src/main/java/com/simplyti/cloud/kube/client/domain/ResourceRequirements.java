package com.simplyti.cloud.kube.client.domain;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResourceRequirements {
	
	private final ContainerResources limits;
	private final ContainerResources requests;

	@JsonCreator
	public ResourceRequirements(
			@JsonProperty("limits") ContainerResources limits,
			@JsonProperty("requests ") ContainerResources requests){
		this.limits=limits;
		this.requests=requests;
	}

}
