package com.simplyti.cloud.kube.client.reqs;

import java.util.Collection;
import java.util.Map;

import com.simplyti.cloud.kube.client.domain.Metadata;
import com.simplyti.cloud.kube.client.domain.Service;
import com.simplyti.cloud.kube.client.domain.ServicePort;
import com.simplyti.cloud.kube.client.domain.ServiceSpec;

import io.netty.handler.codec.http.HttpMethod;

public class CreateServiceRequest extends KubernetesApiRequest {
	
	public CreateServiceRequest(String namespace, String name, Collection<ServicePort> ports, Map<String, String> selector, Map<String, String> annotations) {
		super(HttpMethod.POST, "/api/v1/namespaces/"+namespace+"/services",serviceResource(namespace,name,ports,selector,annotations),Service.class);
	}

	private static Service serviceResource(String namespace, String name, Collection<ServicePort> ports,
			Map<String, String> selector, Map<String, String> annotations) {
		return new Service(Service.KIND, Service.API, 
				Metadata.builder()
				.name(name)
				.namespace(namespace)
				.annotations(annotations)
				.build(), 
				ServiceSpec.builder()
				.ports(ports)
				.selector(selector)
				.build());
	}

}
