package com.simplyti.cloud.kube.client.reqs;


import com.simplyti.cloud.kube.client.domain.Service;

import io.netty.handler.codec.http.HttpMethod;

public class UpdateServiceRequest extends KubernetesApiRequest {

	public UpdateServiceRequest(Service service) {
		super(HttpMethod.PUT, "/api/v1/namespaces/"+service.getMetadata().getNamespace()+"/services/"+service.getMetadata().getName(),service,Service.class);
	}

}
