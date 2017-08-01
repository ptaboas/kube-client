package com.simplyti.cloud.kube.client.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Container {
	
	private final String name;
	private final String image;
	private final Probe readinessProbe;
	
}
