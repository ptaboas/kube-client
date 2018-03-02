package com.simplyti.cloud.kube.client.exception;

import com.simplyti.cloud.kube.client.domain.KubernetesStatus;

public class KubeClientErrorException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 149103805442835138L;
	
	public KubeClientErrorException(KubernetesStatus message, int code) {
		super("Received status "+code+": "+message.getMessage());
	}

	public KubeClientErrorException(String message) {
		super(message);
	}

}
