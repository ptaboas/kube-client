package com.simplyti.cloud.kube.client.pods;

import java.util.ArrayList;
import java.util.List;

import com.simplyti.cloud.kube.client.AbstractCreationBuilder;
import com.simplyti.cloud.kube.client.InternalClient;
import com.simplyti.cloud.kube.client.domain.Container;
import com.simplyti.cloud.kube.client.domain.ImagePullSecret;
import com.simplyti.cloud.kube.client.domain.Metadata;
import com.simplyti.cloud.kube.client.domain.Pod;
import com.simplyti.cloud.kube.client.domain.PodSpec;
import com.simplyti.cloud.kube.client.domain.Volume;

public class PodCreationBuilder extends AbstractCreationBuilder<Pod,PodCreationBuilder>{
	
	public static final String KIND = "Pod";
	
	private final List<Container> containers = new ArrayList<>();
	private final List<Volume> volumes = new ArrayList<>();
	private final List<ImagePullSecret> imagePullsecrets = new ArrayList<>();

	private boolean hostNetwork;

	public PodCreationBuilder(InternalClient client, String api, String namespace) {
		super(client,api,namespace,"pods");
	}

	@Override
	protected Pod create(Metadata metadata) {
		return new Pod(KIND, api(), 
				metadata,
				PodSpec.builder()
				.hostNetwork(hostNetwork)
				.automountServiceAccountToken(false)
				.containers(containers)
				.volumes(volumes)
				.imagePullSecrets(imagePullsecrets)
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

	public VolumeCreationBuilder withVolume() {
		return new VolumeCreationBuilder(this);
	}

	public PodCreationBuilder addVolume(Volume volume) {
		volumes.add(volume);
		return this;
	}

	public PodCreationBuilder withImagePullSecret(String name) {
		imagePullsecrets.add(new ImagePullSecret(name));
		return this;
	}

	public PodCreationBuilder withHostNetwork(boolean hostNetwork) {
		this.hostNetwork=hostNetwork;
		return this;
	}


}
