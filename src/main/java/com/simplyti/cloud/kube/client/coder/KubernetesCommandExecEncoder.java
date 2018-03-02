package com.simplyti.cloud.kube.client.coder;

import java.util.List;

import com.simplyti.cloud.kube.client.reqs.KubernetesCommandExec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpVersion;

@Sharable
public class KubernetesCommandExecEncoder extends MessageToMessageEncoder<KubernetesCommandExec>{
	
	private final String remoteHost;
	
	public KubernetesCommandExecEncoder(String remoteHost) {
		this.remoteHost=remoteHost;
	}
	
	@Override
	protected void encode(ChannelHandlerContext ctx, KubernetesCommandExec msg, List<Object> out) throws Exception {
		FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, msg.getMethod(), msg.getUri());
		
		request.headers().set(HttpHeaderNames.HOST,remoteHost);
		request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.UPGRADE);
		request.headers().set(HttpHeaderNames.UPGRADE, "SPDY/3.1");
		request.headers().set(HttpHeaderNames.CONTENT_LENGTH, "0");
		out.add(request);
	}

}
