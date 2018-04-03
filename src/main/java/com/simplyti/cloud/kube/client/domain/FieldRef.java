package com.simplyti.cloud.kube.client.domain;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class FieldRef {
	
	private final String fieldPath;
	
	@JsonCreator
	public FieldRef(
			@JsonProperty("fieldPath") String fieldPath){
		this.fieldPath=fieldPath;
	}

}
