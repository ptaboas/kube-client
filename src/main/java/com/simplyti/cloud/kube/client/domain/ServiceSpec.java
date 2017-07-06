package com.simplyti.cloud.kube.client.domain;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ServiceSpec {
	
	private final String clusterIP;
	private final List<ServicePort> ports;
	private final Map<String,String> selector;
	

}
