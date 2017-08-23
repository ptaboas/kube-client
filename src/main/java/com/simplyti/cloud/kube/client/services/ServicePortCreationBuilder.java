package com.simplyti.cloud.kube.client.services;

import com.simplyti.cloud.kube.client.domain.ServicePort;

public class ServicePortCreationBuilder<T extends ServiceCreationBuilder<T>> {

	private final ServiceCreationBuilder<T> builder;
	private Integer port;
	private String name;
	
	public ServicePortCreationBuilder(ServiceCreationBuilder<T> builder){
		this.builder=builder;
	}

	public ServicePortCreationBuilder<T> port(int port) {
		this.port=port;
		return this;
	}
	
	public ServicePortCreationBuilder<T> name(String name) {
		this.name=name;
		return this;
	}

	public T create() {
		return builder.addServicePort(ServicePort.builder()
				.name(name)
				.port(port)
				.build());
	}


}
