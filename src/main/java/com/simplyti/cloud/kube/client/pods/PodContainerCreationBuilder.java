package com.simplyti.cloud.kube.client.pods;

import com.google.common.base.MoreObjects;
import com.simplyti.cloud.kube.client.domain.Container;
import com.simplyti.cloud.kube.client.domain.Probe;

public class PodContainerCreationBuilder {

	private PodCreationBuilder builder;
	
	private String image;
	private String name;
	private Probe readinessProbe;

	public PodContainerCreationBuilder(PodCreationBuilder builder) {
		this.builder=builder;
	}

	public PodContainerCreationBuilder withImage(String image) {
		this.image=image;
		return this;
	}
	
	public PodCreationBuilder create() {
		return builder.addContainer(Container.builder()
				.image(image)
				.name(MoreObjects.firstNonNull(name, image))
				.readinessProbe(readinessProbe).build());
	}

	public PodContainerCreationBuilder withReadinessProbe(Probe readinessProbe) {
		this.readinessProbe=readinessProbe;
		return this;
	}

}
