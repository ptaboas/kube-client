package com.simplyti.cloud.kube.client;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Log4J2LoggerFactory;

public class KubeClientBuilder {
	
	private static final Pattern URL = Pattern.compile("(http|https)?(://)?([^:]+):?(\\d+)?");

	private static final String UNSECURE_SCHEMA = "http";
	private static final Integer DEFAULT_UNSECURE_PORT = 8080;
	private static final String SECURE_SCHEMA = "https";
	private static final Integer DEFAULT_SECURE_PORT = 443;
	private static final ApiServer DEFAULT_SERVER_ADDRESS = new ApiServer("kubernetes.default", DEFAULT_SECURE_PORT,new SslContextProvider(false,null));
	
	private String host;
	private Integer port;
	private String caFile;
	private String tokenFile;
	private boolean verbose;
	private EventLoopGroup eventLoop;

	private boolean secured;

	public KubeClientBuilder server(String url) {
		Matcher matcher = URL.matcher(url);
		checkArgument(matcher.matches());
		String schema = Optional.ofNullable(matcher.group(1)).orElse(UNSECURE_SCHEMA);
		this.secured = schema.equals(SECURE_SCHEMA);
		this.host = matcher.group(3);
		this.port = Optional.ofNullable(matcher.group(4)).map(Integer::parseInt)
				.orElse(secured?DEFAULT_SECURE_PORT:DEFAULT_UNSECURE_PORT);
		return this;
	}
	
	public  KubeClient build() {
		return new  KubeClient(Optional.ofNullable(eventLoop).orElseGet(NioEventLoopGroup::new),
				apiServer(),verbose,new TokenProvider(tokenFile));
	}

	private ApiServer apiServer() {
		return Optional.ofNullable(host).map(h->new ApiServer(h,port,new SslContextProvider(secured,caFile)))
			.orElse(DEFAULT_SERVER_ADDRESS);
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
	
	public KubeClientBuilder withLog4J2Logger() {
		InternalLoggerFactory.setDefaultFactory(Log4J2LoggerFactory.INSTANCE);
		return this;
	}

}
