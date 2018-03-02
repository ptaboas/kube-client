package com.simplyti.cloud.kube.client.domain;


import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class CommandProbe extends Probe{
	
	private final ExecAction exec;

	@JsonCreator
	public CommandProbe(
			@JsonProperty("exec") ExecAction exec,
			@JsonProperty("failureThreshold")int failureThreshold, 
			@JsonProperty("successThreshold") int successThreshold, 
			@JsonProperty("initialDelaySeconds") int initialDelaySeconds, 
			@JsonProperty("periodSeconds") int periodSeconds) {
		super(failureThreshold, successThreshold, initialDelaySeconds, periodSeconds);
		this.exec=exec;
	}

}
