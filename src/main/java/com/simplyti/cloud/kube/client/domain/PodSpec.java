package com.simplyti.cloud.kube.client.domain;


import java.util.List;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PodSpec {
	
	private final Boolean automountServiceAccountToken;
	private final List<Container> containers;
	
	@JsonCreator
	public PodSpec(
			@JsonProperty("automountServiceAccountToken") Boolean automountServiceAccountToken,
			@JsonProperty("containers") List<Container> containers){
		this.automountServiceAccountToken=automountServiceAccountToken;
		this.containers=containers;
	}

}
