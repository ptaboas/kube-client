package com.simplyti.cloud.kube.client.domain;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class Port {
	
	private final String name;
	private final Integer port;
	private final String protocol;
	
	@JsonCreator
	public Port(
			@JsonProperty("name")String name,
			@JsonProperty("port")Integer port,
			@JsonProperty("protocol")String protocol) {
		this.name=name;
		this.port=port;
		this.protocol=protocol;
	}

}
