package com.simplyti.cloud.kube.client;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

public class KubeClientBuilder {
	
	private static final Pattern URL = Pattern.compile("(http|https)?(://)?([^:]+):?(\\d+)?");

	private static final String UNSECURE_SCHEMA = "http";
	private static final Integer DEFAULT_UNSECURE_PORT = 8080;
	private static final String SECURE_SCHEMA = "https";
	private static final Integer DEFAULT_SECURE_PORT = 443;
	private static final Address DEFAULT_SERVER_ADDRESS = new Address("kubernetes.default", DEFAULT_SECURE_PORT);
	
	private Address serverAddress;
	private String caFile;
	private String tokenFile;
	private boolean verbose;
	private EventLoopGroup eventLoop;

	public KubeClientBuilder server(String url) {
		Matcher matcher = URL.matcher(url);
		checkArgument(matcher.matches());
		String schema = Optional.ofNullable(matcher.group(1)).orElse(UNSECURE_SCHEMA);
		boolean secured = schema.equals(SECURE_SCHEMA);
		String host = matcher.group(3);
		Integer port = Optional.ofNullable(matcher.group(4)).map(Integer::parseInt)
				.orElse(secured?DEFAULT_SECURE_PORT:DEFAULT_UNSECURE_PORT);
		this.serverAddress = new Address(host,port);
		return this;
	}
	
	public  KubeClient build() {
		return new  KubeClient(Optional.ofNullable(eventLoop).orElseGet(NioEventLoopGroup::new),
				Optional.ofNullable(serverAddress).orElse(DEFAULT_SERVER_ADDRESS),
				verbose,new SslContextProvider(caFile),new TokenProvider(tokenFile));
	}

	public KubeClientBuilder verbose(boolean verbose) {
		this.verbose=verbose;
		return this;
	}
	
	public KubeClientBuilder eventLoop(EventLoopGroup eventLoop){
		this.eventLoop=eventLoop;
		return this;
	}

	public KubeClientBuilder caFile(String caFile) {
		this.caFile=caFile;
		return this;
	}
	
	public KubeClientBuilder tokenFile(String tokenFile) {
		this.tokenFile=tokenFile;
		return this;
	}

}
