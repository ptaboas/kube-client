package com.simplyti.cloud.kube.client.domain;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IngressBackend {
	
	private final String serviceName;
	private final int servicePort;
	
	@JsonCreator
	public IngressBackend(
			@JsonProperty("serviceName") String serviceName,
			@JsonProperty("servicePort") int servicePort) {
		this.serviceName=serviceName;
		this.servicePort=servicePort;
	}

}
