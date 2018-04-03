package com.simplyti.cloud.kube.client.domain;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class EnvironmentVariableValueFromSpec {
	
	private final FieldRef fieldRef;
	
	@JsonCreator
	public EnvironmentVariableValueFromSpec(
			@JsonProperty("fieldRef") FieldRef fieldRef){
		this.fieldRef=fieldRef;
	}

}
