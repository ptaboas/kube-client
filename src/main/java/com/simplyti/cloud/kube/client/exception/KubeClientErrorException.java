package com.simplyti.cloud.kube.client.exception;

public class KubeClientErrorException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 149103805442835138L;
	
	public KubeClientErrorException(String message, int code) {
		super("Received status "+code+": "+message);
	}

	public KubeClientErrorException(String message) {
		super(message);
	}

}
