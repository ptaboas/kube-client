package com.simplyti.cloud.kube.client;

import java.io.InputStream;

import com.google.common.base.MoreObjects;

import io.netty.channel.nio.NioEventLoopGroup;

public class KubeClientBuilder {
	
	private Address serverAddress;
	private boolean verbose;
	private SecurityOptions securityOptions;
	
	public KubeClientBuilder server(String host, int port) {
		this.serverAddress = new Address(host,port);
		return this;
	}
	
	public  KubeClient build() {
		NioEventLoopGroup eventgrup = new NioEventLoopGroup(1);
		return new  KubeClient(eventgrup.next(),serverAddress,verbose,MoreObjects.firstNonNull(securityOptions, SecurityOptions.NONE));
	}

	public KubeClientBuilder verbose(boolean verbose) {
		this.verbose=verbose;
		return this;
	}

	public KubeClientBuilder secure(InputStream caCertificate, String token) {
		this.securityOptions = new SecurityOptions(true, caCertificate, token);
		return this;
	}

}
