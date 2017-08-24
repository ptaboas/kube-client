package com.simplyti.cloud.kube.client.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Subset {
	
	private final List<Address> addresses;
	private final List<Port> ports;

}
