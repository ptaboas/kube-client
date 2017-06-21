package com.simplyti.cloud.kube.client.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Event<T extends KubernetesResource> {
	
	private final EventType type;
	private final T object;

}
