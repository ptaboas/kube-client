package com.simplyti.cloud.kube.client.domain;


import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PodSpec {
	
	private final Boolean automountServiceAccountToken;
	private final Collection<Container> containers;

}
