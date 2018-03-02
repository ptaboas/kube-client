package com.simplyti.cloud.kube.client.domain;

import java.util.Map;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ServicePort {
	
	private final String name;
	private final Integer port;
	private final ServiceProtocol protocol;
	private final Object targetPort;
	
	@JsonCreator
	public ServicePort(
			@JsonProperty("name") String name,
			@JsonProperty("port") Integer port,
			@JsonProperty("protocol") ServiceProtocol protocol,
			@JsonProperty("selector") Map<String,String> selector,
			@JsonProperty("targetPort") Object targetPort) {
		this.name=name;
		this.port=port;
		this.protocol=protocol;
		this.targetPort=targetPort;
	}
	
	public static ServicePort port(int port) {
		return new ServicePort(null, port, null, null);
	}
}
