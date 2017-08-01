package com.simplyti.cloud.kube.client.domain;

import java.util.Collection;


public abstract class Probe {

	public static Probe exec(Collection<String> command) {
		return new ExecProbe(new ExecAction(command),1,1,1,1);
	}

	public static Probe exec(Collection<String> command, int failureThreshold, int successThreshold, int initialDelaySeconds , int periodSeconds) {
		return new ExecProbe(new ExecAction(command),failureThreshold,successThreshold,initialDelaySeconds,periodSeconds);
	}

}
