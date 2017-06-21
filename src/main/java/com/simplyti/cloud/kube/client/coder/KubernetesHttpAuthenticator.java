package com.simplyti.cloud.kube.client.coder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.ReferenceCountUtil;

import java.util.List;

import com.simplyti.cloud.kube.client.SecurityOptions;

import io.netty.channel.ChannelHandler.Sharable;

@Sharable
public class KubernetesHttpAuthenticator extends MessageToMessageEncoder<HttpRequest>{
	
	private SecurityOptions securityOptions;

	public KubernetesHttpAuthenticator(SecurityOptions securityOptions){
		this.securityOptions=securityOptions;
	}
	

	@Override
	protected void encode(ChannelHandlerContext ctx, HttpRequest msg, List<Object> out) throws Exception {
		msg.headers().set(HttpHeaderNames.AUTHORIZATION,"Bearer "+securityOptions.getToken());
		out.add(ReferenceCountUtil.retain(msg));
		
	}

}
