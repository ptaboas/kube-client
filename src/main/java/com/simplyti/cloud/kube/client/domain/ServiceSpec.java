package com.simplyti.cloud.kube.client.domain;

import java.util.Collection;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ServiceSpec {
	
	private final String clusterIP;
	private final Collection<ServicePort> ports;
	private final Map<String,String> selector;
	private final ServiceType type;
	

}
