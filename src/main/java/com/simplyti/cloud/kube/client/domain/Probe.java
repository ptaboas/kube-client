package com.simplyti.cloud.kube.client.domain;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Probe {
	
	private final int failureThreshold;
	private final int successThreshold;
	private final int initialDelaySeconds;
	private final int periodSeconds;

	public static Probe exec(Collection<String> command, int failureThreshold, int successThreshold, int initialDelaySeconds , int periodSeconds) {
		return new CommandProbe(new ExecAction(command),failureThreshold,successThreshold,initialDelaySeconds,periodSeconds);
	}

	public static Probe http(String path, int port, int failureThreshold, int successThreshold, int initialDelaySeconds , int periodSeconds) {
		return http(path,port,failureThreshold,successThreshold,initialDelaySeconds,periodSeconds,null);
	}

	public static Probe http(String path, int port, int failureThreshold, int successThreshold, int initialDelaySeconds , int periodSeconds,Collection<HttpHeader> headers) {
		return new HttpProbe(new HttpGet(path, port, headers), failureThreshold, successThreshold, initialDelaySeconds, periodSeconds);
	}

	public static Probe tcp(int port, int failureThreshold, int successThreshold, int initialDelaySeconds , int periodSeconds) {
		return new TcpProbe(new TCPSocket(port), failureThreshold, successThreshold, initialDelaySeconds, periodSeconds);
	}

}
