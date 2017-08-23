package com.simplyti.cloud.kube.client.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResourceList<T> {
	
	private final ListMetadata metadata;
	private final List<T> items;


}
