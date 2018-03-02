package com.simplyti.cloud.kube.client.domain;

import java.util.Collection;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Getter;

@Getter
public abstract class Probe {
	
	private final int failureThreshold;
	private final int successThreshold;
	private final int initialDelaySeconds;
	private final int periodSeconds;
	
	@JsonCreator
	public Probe(
			@JsonProperty("failureThreshold") int failureThreshold,
			@JsonProperty("successThreshold") int successThreshold,
			@JsonProperty("initialDelaySeconds") int initialDelaySeconds,
			@JsonProperty("periodSeconds") int periodSeconds){
		this.failureThreshold=failureThreshold;
		this.successThreshold=successThreshold;
		this.initialDelaySeconds=initialDelaySeconds;
		this.periodSeconds=periodSeconds;
	}

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
