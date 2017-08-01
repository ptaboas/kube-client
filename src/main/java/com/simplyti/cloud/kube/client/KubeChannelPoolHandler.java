package com.simplyti.cloud.kube.client;


import java.util.function.Supplier;

import com.simplyti.cloud.kube.client.coder.KubeApiEventChunkDecoder;
import com.simplyti.cloud.kube.client.coder.KubernetesApiRequestEncoder;
import com.simplyti.cloud.kube.client.coder.KubernetesApiResponseHandler;
import com.simplyti.cloud.kube.client.coder.KubernetesCommandExecEncoder;
import com.simplyti.cloud.kube.client.coder.KubernetesCommandExecResponseHandler;
import com.simplyti.cloud.kube.client.coder.KubernetesHttpAuthenticator;
import com.simplyti.cloud.kube.client.mapper.KubeObjectMapper;

import io.netty.channel.Channel;
import io.netty.channel.pool.AbstractChannelPoolHandler;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;

public class KubeChannelPoolHandler extends AbstractChannelPoolHandler {
	
	private final KubernetesApiRequestEncoder kubernetesApiRequestEncoder;
	private final KubernetesCommandExecEncoder kubernetesCommandExecEncoder;
	private final KubernetesApiResponseHandler kubernetesApiResponseHandler;
	private final KubernetesCommandExecResponseHandler kubernetesCommandExecResponseHandler;
	
	private final KubeApiEventChunkDecoder kubeApiEventChunkDecoder;
	
	private final KubernetesHttpAuthenticator kubernetesHttpAuthenticator;
	
	private final boolean verbose;
	private final Supplier<SslContext> sslContextProvider;
	
	public KubeChannelPoolHandler(String remoteHost, boolean verbose, Supplier<SslContext> sslContextProvider,
			Supplier<String> tokenProvider){
		KubeObjectMapper mapper = new KubeObjectMapper();
		kubernetesApiRequestEncoder = new KubernetesApiRequestEncoder(remoteHost,mapper);
		kubernetesCommandExecEncoder = new KubernetesCommandExecEncoder(remoteHost);
		kubernetesApiResponseHandler = new KubernetesApiResponseHandler(mapper);
		kubernetesCommandExecResponseHandler = new KubernetesCommandExecResponseHandler(verbose);
		kubeApiEventChunkDecoder = new KubeApiEventChunkDecoder();
		this.sslContextProvider=sslContextProvider;
		kubernetesHttpAuthenticator = new KubernetesHttpAuthenticator(tokenProvider);
		this.verbose=verbose;
		
	}

	@Override
	public void channelCreated(Channel ch) throws Exception {
		SslContext sslContext = sslContextProvider.get();
		if(sslContext!=null){
			ch.pipeline().addLast(sslContext.newHandler(ch.alloc()));
		}
		
		if(verbose){
			ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
		}
		ch.pipeline().addLast(new HttpRequestEncoder());
		ch.pipeline().addLast(kubernetesHttpAuthenticator);
		ch.pipeline().addLast(new HttpResponseDecoder());
		ch.pipeline().addLast(kubeApiEventChunkDecoder);
		ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1000000,Delimiters.lineDelimiter()));
		ch.pipeline().addLast(new HttpObjectAggregator(10000000));
		
		ch.pipeline().addLast(kubernetesApiRequestEncoder);
		ch.pipeline().addLast(kubernetesCommandExecEncoder);
		ch.pipeline().addLast(kubernetesCommandExecResponseHandler);
		ch.pipeline().addLast(kubernetesApiResponseHandler);
	}


}
