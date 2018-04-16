package com.simplyti.cloud.kube.client.pods;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.MoreObjects;
import com.simplyti.cloud.kube.client.domain.Container;
import com.simplyti.cloud.kube.client.domain.ContainerPort;
import com.simplyti.cloud.kube.client.domain.ContainerResources;
import com.simplyti.cloud.kube.client.domain.EnvironmentVariable;
import com.simplyti.cloud.kube.client.domain.EnvironmentVariableLiteral;
import com.simplyti.cloud.kube.client.domain.ImagePullPolicy;
import com.simplyti.cloud.kube.client.domain.Probe;
import com.simplyti.cloud.kube.client.domain.ResourceRequirements;
import com.simplyti.cloud.kube.client.domain.VolumeMount;

public class PodContainerCreationBuilder {

	private PodCreationBuilder builder;
	
	private String image;
	private String name;
	private Probe readinessProbe;
	private final List<ContainerPort> containerPorts;
	private final List<EnvironmentVariable> environmentVariables;
	private final List<VolumeMount> volumeMounts;

	private ImagePullPolicy imagePullPolicy;
	private String[] command;

	private ContainerResources containerResourcesLimit;
	private ContainerResources containerResourcesRequest;

	public PodContainerCreationBuilder(PodCreationBuilder builder) {
		this.builder=builder;
		this.containerPorts=new ArrayList<>();
		this.environmentVariables=new ArrayList<>();
		volumeMounts=new ArrayList<>();
	}

	public PodContainerCreationBuilder withImage(String image) {
		this.image=image;
		return this;
	}
	
	public PodContainerCreationBuilder withEnvironmetVariable(String name, String value) {
		addEnvironmentVariable(new EnvironmentVariableLiteral(name, value));
		return this;
	}
	
	public PodCreationBuilder create() {
		return builder.addContainer(Container.builder()
				.image(image)
				.name(MoreObjects.firstNonNull(name, image))
				.env(environmentVariables)
				.readinessProbe(readinessProbe)
				.volumeMounts(volumeMounts)
				.ports(containerPorts)
				.imagePullPolicy(imagePullPolicy)
				.resources(ResourceRequirements.builder()
						.limits(containerResourcesLimit)
						.requests(containerResourcesRequest)
						.build())
				.command(command)
				.build());
	}
	
	public PodContainerCreationBuilder withName(String name) {
		this.name=name;
		return this;
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

	public PodContainerCreationBuilder withVolumeMount(String name, String mountPath) {
		return addVolumeMount(VolumeMount.builder().name(name).mountPath(mountPath).build());
	}
	
	public PodContainerCreationBuilder addVolumeMount(VolumeMount volumeMount) {
		this.volumeMounts.add(volumeMount);
		return this;
	}

	public VolumeMountCreationBuilder withVolumeMount(String name) {
		return new VolumeMountCreationBuilder(name,this);
	}

	public EnvironmentVariableCreationBuilder withEnvironmetVariable(String name) {
		return new EnvironmentVariableCreationBuilder(name,this);
	}

	public PodContainerCreationBuilder addEnvironmentVariable(
			EnvironmentVariable environmentVariable) {
		environmentVariables.add(environmentVariable);
		return this;
	}

	public PodContainerCreationBuilder withImagePullPolicy(ImagePullPolicy imagePullPolicy) {
		this.imagePullPolicy=imagePullPolicy;
		return this;
	}

	public PodContainerCreationBuilder withCommand(String[] command) {
		this.command=command;
		return this;
	}

	public ContainerResourcesBuilder withRequestResources() {
		return new ContainerResourcesBuilder(this, ContainerResourcesBuilder.Type.REQUEST);
	}
	
	public ContainerResourcesBuilder withLimitResources() {
		return new ContainerResourcesBuilder(this, ContainerResourcesBuilder.Type.LIMIT);
	}

	public PodContainerCreationBuilder setResourceLimit(ContainerResources containerResourcesLimit) {
		this.containerResourcesLimit = containerResourcesLimit;
		return this;
	}

	public PodContainerCreationBuilder setResourceRequest(ContainerResources containerResourcesRequest) {
		this.containerResourcesRequest = containerResourcesRequest;
		return this;
	}

}
