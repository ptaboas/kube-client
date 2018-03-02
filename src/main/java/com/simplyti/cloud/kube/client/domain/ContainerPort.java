package com.simplyti.cloud.kube.client.domain;


import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContainerPort {
	
	private final String name;
	private final Integer containerPort;
	
	@JsonCreator
	public ContainerPort(
			@JsonProperty("name") String name,
			@JsonProperty("containerPort") Integer containerPort){
		this.name=name;
		this.containerPort=containerPort;
	}

}
