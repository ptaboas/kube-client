package com.simplyti.cloud.kube.client.domain;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class ImagePullSecret {
	
	private final String name;

	@JsonCreator
	public ImagePullSecret(
			@JsonProperty("name") String name){
		this.name=name;
	}

}
