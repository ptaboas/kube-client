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
	private final List<ContainerPort> ports;
	private final Probe readinessProbe;
	
	@JsonCreator
	public Container(
			@JsonProperty("name") String name,
			@JsonProperty("image") String image,
			@JsonProperty("ports") List<ContainerPort> ports,
			@JsonProperty("readinessProbe") Probe readinessProbe){
		this.name=name;
		this.image=image;
		this.ports=ports;
		this.readinessProbe=readinessProbe;
	}
	
}
