package com.simplyti.cloud.kube.client.domain;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IngressRule {
	
	private final String host;
	private final IngressHttp http;
	
	@JsonCreator
	public IngressRule(
			@JsonProperty("host") String host,
			@JsonProperty("http") IngressHttp http) {
		this.host=host;
		this.http=http;
	}

}
