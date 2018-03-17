package com.simplyti.cloud.kube.client.domain;

import java.util.List;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IngressSpec {
	
	private final List<IngressRule> rules;
	private final List<IngressTls> tls;

	@JsonCreator
	public IngressSpec(
			@JsonProperty("rules") List<IngressRule> rules,
			@JsonProperty("tls") List<IngressTls> tls) {
		this.rules=rules;
		this.tls=tls;
	}

}
