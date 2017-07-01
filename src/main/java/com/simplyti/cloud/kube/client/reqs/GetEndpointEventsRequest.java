package com.simplyti.cloud.kube.client.reqs;

import com.simplyti.cloud.kube.client.domain.EndpointEvent;

import io.netty.handler.codec.http.HttpMethod;

public class GetEndpointEventsRequest extends KubernetesApiRequest {
	
	public GetEndpointEventsRequest(String namespace, String version) {
		super(HttpMethod.GET, "/api/v1/namespaces/"+namespace+"/endpoints?watch"+(version!=null?"&resourceVersion="+version:""),null,EndpointEvent.class);
	}

	public GetEndpointEventsRequest(String version) {
		super(HttpMethod.GET, "/api/v1/endpoints?watch"+(version!=null?"&resourceVersion="+version:""),null,EndpointEvent.class);
	}


}
