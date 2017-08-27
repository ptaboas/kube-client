package com.simplyti.cloud.kube.client.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HttpHeader {
	
	private final CharSequence name;
	private final CharSequence value;

}
