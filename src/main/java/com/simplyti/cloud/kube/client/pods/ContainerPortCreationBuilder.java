package com.simplyti.cloud.kube.client.pods;

import com.simplyti.cloud.kube.client.domain.ContainerPort;

public class ContainerPortCreationBuilder  {

	private PodContainerCreationBuilder builder;
	private int port;
	private String name;

	public ContainerPortCreationBuilder(PodContainerCreationBuilder builder) {
		this.builder=builder;
	}

	public ContainerPortCreationBuilder port(int port) {
		this.port=port;
		return this;
	}
	
	public ContainerPortCreationBuilder name(String name) {
		this.name=name;
		return this;
	}
	
	public PodContainerCreationBuilder create() {
		return builder.addContainerPort(ContainerPort.builder()
				.name(name)
				.containerPort(port)
				.build());
	}

}
