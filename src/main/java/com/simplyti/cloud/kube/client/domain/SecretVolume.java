package com.simplyti.cloud.kube.client.domain;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class SecretVolume extends Volume {

	private final SecretVolumeSpec secret;

	@JsonCreator
	public SecretVolume(
			@JsonProperty("name") String name,
			@JsonProperty("secret") SecretVolumeSpec secret) {
		super(name);
		this.secret=secret;
	}

}
