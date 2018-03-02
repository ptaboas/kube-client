package com.simplyti.cloud.kube.client;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiServer {
	
	private final String host;
	private final Integer port;
	private final SslContextProvider sslContextProvider;

}