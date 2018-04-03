package com.simplyti.cloud.kube.client.domain;


import java.util.List;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PodSpec {
	
	private final Boolean automountServiceAccountToken;
	private final List<Container> containers;
	private final List<Volume> volumes;
	private final List<ImagePullSecret> imagePullSecrets;
	private final Boolean hostNetwork;
	
	@JsonCreator
	public PodSpec(
			@JsonProperty("automountServiceAccountToken") Boolean automountServiceAccountToken,
			@JsonProperty("containers") List<Container> containers,
			@JsonProperty("volumes") List<Volume> volumes,
			@JsonProperty("imagePullSecrets") List<ImagePullSecret> imagePullSecrets,
			@JsonProperty("hostNetwork") Boolean hostNetwork){
		this.automountServiceAccountToken=automountServiceAccountToken;
		this.containers=containers;
		this.volumes=volumes;
		this.imagePullSecrets=imagePullSecrets;
		this.hostNetwork=hostNetwork;
	}

}
