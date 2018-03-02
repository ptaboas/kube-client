package com.simplyti.cloud.kube.client.domain;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class ContainerStatus {
	
	private final Boolean ready;
	
	@JsonCreator
	public ContainerStatus(
			@JsonProperty("ready") Boolean ready){
		this.ready=ready;
	}
	
}
