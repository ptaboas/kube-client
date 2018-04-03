package com.simplyti.cloud.kube.client.pods;

import com.simplyti.cloud.kube.client.domain.VolumeMount;

public class VolumeMountCreationBuilder {

	private final String name;
	private final PodContainerCreationBuilder builder;
	private String mountPath;

	public VolumeMountCreationBuilder(String name, PodContainerCreationBuilder builder) {
		this.name=name;
		this.builder=builder;
	}

	public VolumeMountCreationBuilder mount(String mountPath) {
		this.mountPath=mountPath;
		return this;
	}

	public PodContainerCreationBuilder create() {
		return builder.addVolumeMount(VolumeMount.builder().name(name).mountPath(mountPath).build());
	}

	
}
