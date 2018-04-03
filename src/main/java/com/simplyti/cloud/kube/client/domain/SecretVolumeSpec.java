package com.simplyti.cloud.kube.client.domain;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class SecretVolumeSpec {
	
	private final String secretName;

	@JsonCreator
	public SecretVolumeSpec(
			@JsonProperty("secretName") String secretName) {
		this.secretName=secretName;
	}

}
