package com.simplyti.cloud.kube.client.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ServicePort {
	
	private final String name;
	private final Integer port;
	private final ServiceProtocol protocol;
	private final Object targetPort;
	
	
	public static ServicePort port(int port) {
		return new ServicePort(null, port, null, null);
	}

}
