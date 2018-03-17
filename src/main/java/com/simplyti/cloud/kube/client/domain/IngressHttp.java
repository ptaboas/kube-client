package com.simplyti.cloud.kube.client.domain;

import java.util.List;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IngressHttp {

	private final List<IngressPath> paths;
	
	@JsonCreator
	public IngressHttp(
			@JsonProperty("paths") List<IngressPath> paths) {
		this.paths=paths;
	}
}
