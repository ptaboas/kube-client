package com.simplyti.cloud.kube.client.domain;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Container {
	
	private final String name;
	private final String image;
	private final Collection<ContainerPort> ports;
	private final Probe readinessProbe;
	
}
