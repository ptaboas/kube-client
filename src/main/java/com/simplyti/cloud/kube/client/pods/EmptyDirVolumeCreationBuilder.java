package com.simplyti.cloud.kube.client.pods;

import com.simplyti.cloud.kube.client.domain.EmptyDirVolume;
import com.simplyti.cloud.kube.client.domain.EmptyDirVolumeSpec;

public class EmptyDirVolumeCreationBuilder {

	private final VolumeCreationBuilder builder;
	private String name;

	public EmptyDirVolumeCreationBuilder(VolumeCreationBuilder builder, String name) {
		this.builder=builder;
		this.name=name;
	}

	public PodCreationBuilder create() {
		return builder.addVolume(new EmptyDirVolume(name,new EmptyDirVolumeSpec()));
	}

}
