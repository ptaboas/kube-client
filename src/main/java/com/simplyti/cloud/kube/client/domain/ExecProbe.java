package com.simplyti.cloud.kube.client.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExecProbe extends Probe{

	private final ExecAction exec;
	private final int failureThreshold;
	private final int successThreshold;
	private final int initialDelaySeconds;
	private final int periodSeconds;
}
