package com.simplyti.cloud.kube.client.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Namespace {
	
	private final String kind;
	private final String apiVersion;
	private final Metadata metadata;

}
