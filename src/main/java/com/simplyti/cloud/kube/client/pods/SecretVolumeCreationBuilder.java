package com.simplyti.cloud.kube.client.pods;

import com.simplyti.cloud.kube.client.domain.SecretVolume;
import com.simplyti.cloud.kube.client.domain.SecretVolumeSpec;

public class SecretVolumeCreationBuilder {
	
	private final VolumeCreationBuilder builder;
	private String name;
	private String secretName;

	public SecretVolumeCreationBuilder(VolumeCreationBuilder builder, String name) {
		this.builder=builder;
		this.name=name;
	}
	
	public PodCreationBuilder create() {
		return builder.addVolume(new SecretVolume(name,new SecretVolumeSpec(secretName)));
	}

	public SecretVolumeCreationBuilder name(String secretName) {
		this.secretName=secretName;
		return this;
	}

}
