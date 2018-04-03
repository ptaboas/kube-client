package com.simplyti.cloud.kube.client.domain;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class EnvironmentVariableValueFrom extends EnvironmentVariable{
	
	private final EnvironmentVariableValueFromSpec valueFrom;
	
	@JsonCreator
	public EnvironmentVariableValueFrom(
			@JsonProperty("name") String name,
			@JsonProperty("valueFrom") EnvironmentVariableValueFromSpec valueFrom){
		super(name);
		this.valueFrom=valueFrom;
	}

}
