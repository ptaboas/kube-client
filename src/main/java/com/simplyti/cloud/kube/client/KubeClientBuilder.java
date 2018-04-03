package com.simplyti.cloud.kube.client;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Suppliers;

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
	private static final ApiServer DEFAULT_SERVER_ADDRESS = new ApiServer("kubernetes.default", DEFAULT_SECURE_PORT,
			new SslContextProvider(true, null));

	private String caFile;
	private String tokenFile;
	private String token;
	private boolean verbose;
	private EventLoopGroup eventLoop;

	private String apiServer;

	public KubeClientBuilder server(String apiServer) {
		this.apiServer=apiServer;
		return this;
	}

	public KubeClient build() {
		final Supplier<String> tokenProvider;
		if(token==null) {
			tokenProvider = new TokenProvider(tokenFile);
		}else {
			tokenProvider = Suppliers.ofInstance(token);
		}
		return new KubeClient(Optional.ofNullable(eventLoop).orElseGet(NioEventLoopGroup::new), apiServer(), verbose,tokenProvider);
	}

	private ApiServer apiServer() {
		return Optional.ofNullable(apiServer).map(server ->{ 
				Matcher matcher = URL.matcher(server);
				checkArgument(matcher.matches());
				String schema = Optional.ofNullable(matcher.group(1)).orElse(UNSECURE_SCHEMA);
				boolean secured = schema.equals(SECURE_SCHEMA);
				String host = matcher.group(3);
				int port = Optional.ofNullable(matcher.group(4)).map(Integer::parseInt)
						.orElse(secured ? DEFAULT_SECURE_PORT : DEFAULT_UNSECURE_PORT);
				return new ApiServer(host, port, new SslContextProvider(secured, caFile));
			}).orElse(DEFAULT_SERVER_ADDRESS);
	}

	public KubeClientBuilder verbose(boolean verbose) {
		this.verbose = verbose;
		return this;
	}

	public KubeClientBuilder eventLoop(EventLoopGroup eventLoop) {
		this.eventLoop = eventLoop;
		return this;
	}

	public KubeClientBuilder caFile(String caFile) {
		this.caFile = caFile;
		return this;
	}

	public KubeClientBuilder tokenFile(String tokenFile) {
		this.tokenFile = tokenFile;
		return this;
	}

	public KubeClientBuilder withLog4J2Logger() {
		InternalLoggerFactory.setDefaultFactory(Log4J2LoggerFactory.INSTANCE);
		return this;
	}

	public KubeClientBuilder token(String token) {
		this.token = token;
		return this;
	}

}
