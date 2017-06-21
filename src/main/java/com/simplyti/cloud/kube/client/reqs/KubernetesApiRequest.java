package com.simplyti.cloud.kube.client.reqs;

import io.netty.handler.codec.http.HttpMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KubernetesApiRequest {
	
	private final HttpMethod method;
	private final String uri;
	private final Object body;
	private final Class<?> responseClass;

}
