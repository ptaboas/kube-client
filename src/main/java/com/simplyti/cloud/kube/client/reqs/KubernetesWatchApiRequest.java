package com.simplyti.cloud.kube.client.reqs;

import io.netty.handler.codec.http.HttpMethod;

public class KubernetesWatchApiRequest extends KubernetesApiRequest{
	
	private static final String EMPTY = "";

	public KubernetesWatchApiRequest(String uri,String version) {
		super(HttpMethod.GET, uri+"?watch"+(version!=null?"&resourceVersion="+version:EMPTY), null);
	}

}
