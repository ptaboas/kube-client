package com.simplyti.cloud.kube.client.domain;

import java.util.List;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class PodStatus {
	
	private final List<ContainerStatus> containerStatuses;
	private final String hostIP;
	private final String podIP;
	private final PodPhase phase;
	
	@JsonCreator
	public PodStatus(
			@JsonProperty("containerStatuses") List<ContainerStatus> containerStatuses,
			@JsonProperty("hostIP") String hostIP,
			@JsonProperty("podIP") String podIP,
			@JsonProperty("phase") PodPhase phase){
		this.containerStatuses=containerStatuses;
		this.hostIP=hostIP;
		this.podIP=podIP;
		this.phase=phase;
	}

}
