package com.simplyti.cloud.kube.client.domain;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class Address {
	
	private final String ip;
	
	@JsonCreator
	public Address(
			@JsonProperty("ip")String ip) {
		this.ip=ip;
	}

}
