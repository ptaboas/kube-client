package com.simplyti.cloud.kube.client.domain;

import java.util.List;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class ResourceList<T> {
	
	private final ListMetadata metadata;
	private final List<T> items;
	
	@JsonCreator
	public ResourceList(
			@JsonProperty("metadata") ListMetadata metadata,
			@JsonProperty("items")  List<T> items){
		this.metadata=metadata;
		this.items=items;
	}


}
