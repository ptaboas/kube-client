package com.simplyti.cloud.kube.client;


import com.simplyti.cloud.kube.client.coder.KubeApiEventChunkDecoder;
import com.simplyti.cloud.kube.client.coder.KubernetesApiRequestEncoder;
import com.simplyti.cloud.kube.client.coder.KubernetesApiResponseHandler;
import com.simplyti.cloud.kube.client.coder.KubernetesHttpAuthenticator;

import io.netty.channel.Channel;
import io.netty.channel.pool.AbstractChannelPoolHandler;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class KubeChannelPoolHandler extends AbstractChannelPoolHandler {
	
	private final KubernetesApiRequestEncoder kubernetesApiRequestEncoder;
	private final KubernetesApiResponseHandler kubernetesApiResponseHandler;
	
	private final KubeApiEventChunkDecoder kubeApiEventChunkDecoder;
	
	private final KubernetesHttpAuthenticator kubernetesHttpAuthenticator;
	
	private final boolean verbose;
	private final SecurityOptions securityOptions;
	
	public KubeChannelPoolHandler(String remoteHost, boolean verbose,SecurityOptions securityOptions){
		kubernetesApiRequestEncoder = new KubernetesApiRequestEncoder(remoteHost);
		kubernetesApiResponseHandler = new KubernetesApiResponseHandler();
		kubeApiEventChunkDecoder = new KubeApiEventChunkDecoder();
		kubernetesHttpAuthenticator = new KubernetesHttpAuthenticator(securityOptions);
		this.verbose=verbose;
		this.securityOptions=securityOptions;
	}

	@Override
	public void channelCreated(Channel ch) throws Exception {
		if(securityOptions.isSsl()){
			ch.pipeline().addLast(securityOptions.getSslCtx().newHandler(ch.alloc()));
		}
		
		if(verbose){
			ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
		}
		ch.pipeline().addLast(new HttpRequestEncoder());
		if(securityOptions.isSsl()){
			ch.pipeline().addLast(kubernetesHttpAuthenticator);
		}
		ch.pipeline().addLast(new HttpResponseDecoder());
		ch.pipeline().addLast(kubeApiEventChunkDecoder);
		ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1000000,Delimiters.lineDelimiter()));
		ch.pipeline().addLast(new HttpObjectAggregator(10000000));
		
		ch.pipeline().addLast(kubernetesApiRequestEncoder);
		ch.pipeline().addLast(kubernetesApiResponseHandler);
	}


}
