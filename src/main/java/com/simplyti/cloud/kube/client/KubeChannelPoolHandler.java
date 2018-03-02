package com.simplyti.cloud.kube.client;

import java.nio.channels.ClosedChannelException;
import java.util.Optional;
import java.util.function.Supplier;

import com.simplyti.cloud.kube.client.coder.ChannelIddleHandler;
import com.simplyti.cloud.kube.client.coder.KubeApiEventChunkDecoder;
import com.simplyti.cloud.kube.client.coder.KubernetesApiRequestEncoder;
import com.simplyti.cloud.kube.client.coder.KubernetesApiResponseHandler;
import com.simplyti.cloud.kube.client.coder.KubernetesCommandExecEncoder;
import com.simplyti.cloud.kube.client.coder.KubernetesCommandExecResponseHandler;
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
import io.netty.handler.ssl.SslContext;
import io.netty.handler.timeout.IdleStateHandler;

public class KubeChannelPoolHandler extends AbstractChannelPoolHandler {
	
	private final ChannelIddleHandler channelIddleHandler;
	
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
		kubernetesApiRequestEncoder = new KubernetesApiRequestEncoder(remoteHost);
		kubernetesCommandExecEncoder = new KubernetesCommandExecEncoder(remoteHost);
		kubernetesApiResponseHandler = new KubernetesApiResponseHandler();
		kubernetesCommandExecResponseHandler = new KubernetesCommandExecResponseHandler(verbose);
		kubeApiEventChunkDecoder = new KubeApiEventChunkDecoder();
		this.sslContextProvider=sslContextProvider;
		kubernetesHttpAuthenticator = new KubernetesHttpAuthenticator(tokenProvider);
		this.channelIddleHandler=new ChannelIddleHandler();
		this.verbose=verbose;
		
	}

	@Override
	public void channelCreated(Channel ch) throws Exception {
		ch.closeFuture().addListener(futureClose->{
			Optional.ofNullable(ch.attr(InternalClient.SINGLE_RESPONSE_PROMISE).get())
				.ifPresent(currentPromise->currentPromise.tryFailure(new ClosedChannelException()));
		});
		
		ch.pipeline().addLast(new IdleStateHandler(0,0,30));
		ch.pipeline().addLast(this.channelIddleHandler);
		
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
