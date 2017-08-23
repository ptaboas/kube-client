package com.simplyti.cloud.kube.client.pods;

import java.util.ArrayList;
import java.util.List;

import com.simplyti.cloud.kube.client.AbstractCreationBuilder;
import com.simplyti.cloud.kube.client.InternalClient;
import com.simplyti.cloud.kube.client.domain.Container;
import com.simplyti.cloud.kube.client.domain.Metadata;
import com.simplyti.cloud.kube.client.domain.Pod;
import com.simplyti.cloud.kube.client.domain.PodSpec;

public class PodCreationBuilder extends AbstractCreationBuilder<Pod,PodCreationBuilder>{
	
	public static final String KIND = "Pod";
	public static final String API = "v1";
	
	private final List<Container> containers = new ArrayList<>();

	public PodCreationBuilder(InternalClient client, String namespace) {
		super(client,namespace,"pods");
	}

	@Override
	protected Pod create(Metadata metadata) {
		return new Pod(KIND, API, 
				metadata,
				PodSpec.builder()
				.automountServiceAccountToken(false)
				.containers(containers)
				.build(),
				null);
	}

	public PodContainerCreationBuilder withContainer() {
		return new PodContainerCreationBuilder(this);
	}

	public PodCreationBuilder addContainer(Container container) {
		containers.add(container);
		return this;
	}


}
