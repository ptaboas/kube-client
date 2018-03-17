package com.simplyti.cloud.kube.client.domain;

import java.util.List;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class IngressTls {
	
	private final List<String> hosts;
	private final String secretName;

	@JsonCreator
	public IngressTls(
			@JsonProperty("hosts") List<String> hosts,
			@JsonProperty("secretName ") String secretName) {
		this.hosts=hosts;
		this.secretName=secretName;
	}

}
