package com.simplyti.cloud.kube.client.domain;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IngressPath {
	
	private final String path;
	private final IngressBackend backend;
	
	@JsonCreator
	public IngressPath(
			@JsonProperty("path") String path,
			@JsonProperty("backend") IngressBackend backend) {
		this.path=path;
		this.backend=backend;
	}

}
