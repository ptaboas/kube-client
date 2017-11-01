package com.simplyti.cloud.kube.client.domain;

import io.netty.util.CharsetUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class SecretData {

	private final byte[] data;
	
	public SecretData(byte[] data) {
		this.data=data;
	}

	public SecretData of(String strData){
		return new SecretData(strData.getBytes(CharsetUtil.UTF_8));
	}

}
