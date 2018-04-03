package com.simplyti.cloud.kube.client.domain;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VolumeMount {
	
	private final String name;
	private final String mountPath;
	
	@JsonCreator
	public VolumeMount(
			@JsonProperty("name") String name,
			@JsonProperty("mountPath") String mountPath){
		this.name=name;
		this.mountPath=mountPath;
	}

}
