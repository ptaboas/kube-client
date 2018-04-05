package com.simplyti.cloud.kube.client.domain;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class NamespaceStatus {
	
	private final NamespacePhase phase;

	@JsonCreator
	public NamespaceStatus(
			@JsonProperty("phase") NamespacePhase phase){
		this.phase=phase;
	}

}
