package com.simplyti.cloud.kube.client.domain;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Metadata {
	
	private final String name;
	private final String generateName;
	private final String namespace;
	private final String selfLink;
	private final String uid;
	private final String resourceVersion;
	private final LocalDateTime creationTimestamp;
	private final Map<String,String> labels;
	private final Map<String,String> annotations;
	
}
