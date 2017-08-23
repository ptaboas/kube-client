package com.simplyti.cloud.kube.client.reqs;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KubernetesCommandExec {
	
	private final String namespace;
	private final String name;
	private final String container;
	private final String command;

}
