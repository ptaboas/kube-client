package com.simplyti.cloud.kube.client.reqs;

import com.simplyti.cloud.kube.client.domain.PodEvent;

import io.netty.handler.codec.http.HttpMethod;

public class GetPodEventsRequest extends KubernetesApiRequest {
	
	public GetPodEventsRequest(String version) {
		super(HttpMethod.GET, "/api/v1/pods?watch&resourceVersion="+version,null,PodEvent.class);
	}


}
