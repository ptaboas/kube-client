package com.simplyti.cloud.kube.client.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Port {
	
	private final String name;
	private final Integer port;
	private final String protocol;

}
