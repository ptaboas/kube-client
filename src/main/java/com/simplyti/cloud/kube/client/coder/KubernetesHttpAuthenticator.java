package com.simplyti.cloud.kube.client.coder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.ReferenceCountUtil;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Supplier;

import io.netty.channel.ChannelHandler.Sharable;

@Sharable
@RequiredArgsConstructor
public class KubernetesHttpAuthenticator extends MessageToMessageEncoder<HttpRequest>{
	
	private final Supplier<String> tokenProvider;

	@Override
	protected void encode(ChannelHandlerContext ctx, HttpRequest msg, List<Object> out) throws Exception {
		String token = tokenProvider.get();
		if(token!=null){
			msg.headers().set(HttpHeaderNames.AUTHORIZATION,"Bearer "+token);
		}
		out.add(ReferenceCountUtil.retain(msg));
	}

}
