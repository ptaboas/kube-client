package com.simplyti.cloud.kube.client.services;

import com.simplyti.cloud.kube.client.domain.ServicePort;
import com.simplyti.cloud.kube.client.domain.ServiceProtocol;

public class ServicePortCreationBuilder<T extends ServiceCreationBuilder<T>> {

	private final ServiceCreationBuilder<T> builder;
	private Integer port;
	private String name;
	private ServiceProtocol protocol;
	private String targetPort;
	
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
	
	public ServicePortCreationBuilder<T> protocol(ServiceProtocol protocol) {
		this.protocol=protocol;
		return this;
	}
	
	public ServicePortCreationBuilder<T> targetPort(String targetPort) {
		this.targetPort=targetPort;
		return this;
	}

	public T create() {
		return builder.addServicePort(ServicePort.builder()
				.name(name)
				.port(port)
				.targetPort(targetPort)
				.protocol(protocol)
				.build());
	}


}
