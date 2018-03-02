package com.simplyti.cloud.kube.client.domain;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class HttpProbe extends Probe{
	
	private final HttpGet httpGet;

	@JsonCreator
	public HttpProbe(
			@JsonProperty("httpGet") HttpGet httpGet,
			@JsonProperty("failureThreshold")int failureThreshold, 
			@JsonProperty("successThreshold") int successThreshold, 
			@JsonProperty("initialDelaySeconds") int initialDelaySeconds, 
			@JsonProperty("periodSeconds") int periodSeconds) {
		super(failureThreshold, successThreshold, initialDelaySeconds, periodSeconds);
		this.httpGet=httpGet;
	}

}
