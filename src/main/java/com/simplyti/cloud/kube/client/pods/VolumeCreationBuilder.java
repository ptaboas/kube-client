package com.simplyti.cloud.kube.client.pods;

import com.simplyti.cloud.kube.client.domain.Volume;

public class VolumeCreationBuilder {

	private final PodCreationBuilder builder;
	private String name;

	public VolumeCreationBuilder(PodCreationBuilder builder) {
		this.builder=builder;
	}
	
	public VolumeCreationBuilder withName(String name) {
		this.name=name;
		return this;
	}

	public EmptyDirVolumeCreationBuilder emptyDir() {
		return new EmptyDirVolumeCreationBuilder(this,name);
	}
	
	public SecretVolumeCreationBuilder secret() {
		return new SecretVolumeCreationBuilder(this,name);
	}

	public PodCreationBuilder addVolume(Volume volume) {
		return builder.addVolume(volume);
	}

}
