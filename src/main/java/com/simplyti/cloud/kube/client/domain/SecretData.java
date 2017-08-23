package com.simplyti.cloud.kube.client.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@AllArgsConstructor
public class SecretData {

	private final String value;

	public String getStringValue() {
		return value;
	}

}
