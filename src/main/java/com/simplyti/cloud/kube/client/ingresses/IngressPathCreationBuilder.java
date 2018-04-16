package com.simplyti.cloud.kube.client.ingresses;

import com.simplyti.cloud.kube.client.domain.IngressBackend;
import com.simplyti.cloud.kube.client.domain.IngressPath;

public class IngressPathCreationBuilder<T extends IngressCreationBuilder<T>> {

	private final IngressRuleCreationBuilder<T> builder;
	private final String path;
	private String serviceName;
	private int servicePort;

	public IngressPathCreationBuilder(IngressRuleCreationBuilder<T> builder, String path) {
		this.path=path;
		this.builder=builder;
	}
	
	public IngressRuleCreationBuilder<T> create() {
		return builder.addPath(IngressPath.builder().path(path)
				.backend(IngressBackend.builder().serviceName(serviceName).servicePort(servicePort).build()).build());
	}

	public IngressPathCreationBuilder<T> backendServiceName(String serviceName) {
		this.serviceName=serviceName;
		return this;
	}

	public IngressPathCreationBuilder<T> backendServicePort(int servicePort) {
		this.servicePort=servicePort;
		return this;
	}

}
