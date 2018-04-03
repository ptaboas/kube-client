package com.simplyti.cloud.kube.client.domain;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class EnvironmentVariableLiteral extends EnvironmentVariable{
	
	private final String value;
	
	@JsonCreator
	public EnvironmentVariableLiteral(
			@JsonProperty("name") String name,
			@JsonProperty("value") String value){
		super(name);
		this.value=value;
	}

}
