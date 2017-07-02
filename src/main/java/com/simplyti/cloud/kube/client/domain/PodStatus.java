package com.simplyti.cloud.kube.client.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PodStatus {
	
	private final List<ContainerStatus> containerStatuses;
	private final String hostIP;
	private final String podIP;
	private final PodPhase phase;

}
