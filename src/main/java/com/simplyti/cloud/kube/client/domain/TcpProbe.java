package com.simplyti.cloud.kube.client.domain;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class TcpProbe extends Probe{
	
	private final TCPSocket tcpSocket;
	
	@JsonCreator
	public TcpProbe(
			@JsonProperty("tcpSocket") TCPSocket tcpSocket,
			@JsonProperty("failureThreshold")int failureThreshold, 
			@JsonProperty("successThreshold") int successThreshold, 
			@JsonProperty("initialDelaySeconds") int initialDelaySeconds, 
			@JsonProperty("periodSeconds") int periodSeconds) {
		super(failureThreshold, successThreshold, initialDelaySeconds, periodSeconds);
		this.tcpSocket=tcpSocket;
	}

}
