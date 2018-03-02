package com.simplyti.cloud.kube.client.domain;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class ListMetadata {
	
	private final String resourceVersion;
	
	@JsonCreator
	public ListMetadata(
			@JsonProperty("resourceVersion") String resourceVersion){
		this.resourceVersion=resourceVersion;
	}

}
