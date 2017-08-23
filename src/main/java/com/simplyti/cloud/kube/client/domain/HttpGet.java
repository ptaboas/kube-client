package com.simplyti.cloud.kube.client.domain;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HttpGet {

	private final String path;
	private final int port;
	private Collection<HttpHeader> httpHeaders;
}
