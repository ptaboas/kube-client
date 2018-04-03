package com.simplyti.cloud.kube.client.domain;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class EmptyDirVolume extends Volume {
	
	private final EmptyDirVolumeSpec emptyDir;

	@JsonCreator
	public EmptyDirVolume(
			@JsonProperty("name") String name,
			@JsonProperty("emptyDir") EmptyDirVolumeSpec emptyDir) {
		super(name);
		this.emptyDir=emptyDir;
	}

}
