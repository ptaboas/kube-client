package com.simplyti.cloud.kube.client.domain;

import java.util.List;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Container {
	
	private final String name;
	private final String image;
	private final List<EnvironmentVariable> env;
	private final List<ContainerPort> ports;
	private final Probe readinessProbe;
	private final ResourceRequirements resources;
	private final List<VolumeMount> volumeMounts;
	private final ImagePullPolicy imagePullPolicy;
	private final String[] command;
	
	@JsonCreator
	public Container(
			@JsonProperty("name") String name,
			@JsonProperty("image") String image,
			@JsonProperty("env") List<EnvironmentVariable> env,
			@JsonProperty("ports") List<ContainerPort> ports,
			@JsonProperty("readinessProbe") Probe readinessProbe,
			@JsonProperty("resources") ResourceRequirements resources,
			@JsonProperty("volumeMounts") List<VolumeMount> volumeMounts,
			@JsonProperty("imagePullPolicy") ImagePullPolicy imagePullPolicy,
			@JsonProperty("command") String[] command){
		this.name=name;
		this.image=image;
		this.env=env;
		this.ports=ports;
		this.readinessProbe=readinessProbe;
		this.resources=resources;
		this.volumeMounts=volumeMounts;
		this.imagePullPolicy=imagePullPolicy;
		this.command=command;
	}
	
}
