package com.simplyti.cloud.kube.client.pods;

import com.simplyti.cloud.kube.client.domain.ContainerResources;

public class ContainerResourcesBuilder {
	
	public enum Type{
		LIMIT,
		REQUEST;
	}

	private final PodContainerCreationBuilder builder;
	private final Type type;
	
	private String memory;
	private String cpu;

	

	public ContainerResourcesBuilder(PodContainerCreationBuilder builder, Type type) {
		this.builder=builder;
		this.type=type;
	}

	public ContainerResourcesBuilder memmory(String memory) {
		this.memory=memory;
		return this;
	}

	public ContainerResourcesBuilder cpu(String cpu) {
		this.cpu=cpu;
		return this;
	}

	public PodContainerCreationBuilder create() {
		switch(type) {
		case LIMIT:
			return builder.setResourceLimit(new ContainerResources(memory, cpu));
		case REQUEST:
		default:
			return builder.setResourceRequest(new ContainerResources(memory, cpu));
		}
	}

}
