package com.simplyti.cloud.kube.client.reqs;

import com.fasterxml.jackson.core.type.TypeReference;

import io.netty.handler.codec.http.HttpMethod;

public class KubernetesWatchApiRequest extends KubernetesApiRequest{
	
	private static final String EMPTY = "";

	public KubernetesWatchApiRequest(String uri,String version, TypeReference<?> responseClass) {
		super(HttpMethod.GET, uri+"?watch"+(version!=null?"&resourceVersion="+version:EMPTY), null, responseClass);
	}

}
