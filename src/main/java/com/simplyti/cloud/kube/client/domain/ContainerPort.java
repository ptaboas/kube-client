package com.simplyti.cloud.kube.client.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ContainerPort {
	
	private final String name;
	private final Integer containerPort;

}
