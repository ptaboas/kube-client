package com.simplyti.cloud.kube.client.pods;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.MoreObjects;
import com.simplyti.cloud.kube.client.domain.Container;
import com.simplyti.cloud.kube.client.domain.ContainerPort;
import com.simplyti.cloud.kube.client.domain.Probe;

public class PodContainerCreationBuilder {

	private PodCreationBuilder builder;
	
	private String image;
	private String name;
	private Probe readinessProbe;
	private final List<ContainerPort> containerPorts;

	public PodContainerCreationBuilder(PodCreationBuilder builder) {
		this.builder=builder;
		this.containerPorts=new ArrayList<>();
	}

	public PodContainerCreationBuilder withImage(String image) {
		this.image=image;
		return this;
	}
	
	public PodCreationBuilder create() {
		return builder.addContainer(Container.builder()
				.image(image)
				.name(MoreObjects.firstNonNull(name, image))
				.readinessProbe(readinessProbe)
				.ports(containerPorts)
				.build());
	}

	public PodContainerCreationBuilder withReadinessProbe(Probe readinessProbe) {
		this.readinessProbe=readinessProbe;
		return this;
	}

	public ContainerPortCreationBuilder withPort(){
		return new ContainerPortCreationBuilder(this);
	}

	public PodContainerCreationBuilder addContainerPort(ContainerPort containerPort) {
		this.containerPorts.add(containerPort);
		return this;
	}

}
