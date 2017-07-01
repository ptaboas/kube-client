package com.simplyti.cloud.kube.client.domain;

import java.util.Collection;


public interface Probe {

	public static Probe exec(Collection<String> command) {
		return new ExecProbe(new ExecAction(command));
	}

}
