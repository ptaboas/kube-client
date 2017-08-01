package com.simplyti.cloud.kube.client.reqs;

import com.simplyti.cloud.kube.client.domain.ServiceEvent;

import io.netty.handler.codec.http.HttpMethod;

public class GetServiceEventsRequest extends KubernetesApiRequest {

	public GetServiceEventsRequest(String version) {
		super(HttpMethod.GET, "/api/v1/services?watch&resourceVersion="+version,null,ServiceEvent.class);
	}


}
