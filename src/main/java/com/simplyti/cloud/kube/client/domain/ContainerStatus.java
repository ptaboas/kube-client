package com.simplyti.cloud.kube.client.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContainerStatus {
	
	private final Boolean ready;
	
}
