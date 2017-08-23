package com.simplyti.cloud.kube.client.coder;

import java.util.List;

import com.google.common.base.Splitter;
import com.simplyti.cloud.kube.client.reqs.KubernetesCommandExec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringEncoder;

@Sharable
public class KubernetesCommandExecEncoder extends MessageToMessageEncoder<KubernetesCommandExec>{
	
	private final String remoteHost;
	
	public KubernetesCommandExecEncoder(String remoteHost) {
		this.remoteHost=remoteHost;
	}
	
	@Override
	protected void encode(ChannelHandlerContext ctx, KubernetesCommandExec msg, List<Object> out) throws Exception {
		QueryStringEncoder encoder = new QueryStringEncoder("/api/v1/namespaces/"+msg.getNamespace()+"/pods/"+msg.getName()+"/exec");
		Splitter.on(' ').split(msg.getCommand()).forEach(command->encoder.addParam("command", command));
		encoder.addParam("container", msg.getContainer());
		encoder.addParam("stdout", "true");
		encoder.addParam("stderr", "true");
		
		FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, encoder.toString());
		
		request.headers().set(HttpHeaderNames.HOST,remoteHost);
		request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.UPGRADE);
		request.headers().set(HttpHeaderNames.UPGRADE, "SPDY/3.1");
		request.headers().set(HttpHeaderNames.CONTENT_LENGTH, "0");
		out.add(request);
	}

}
