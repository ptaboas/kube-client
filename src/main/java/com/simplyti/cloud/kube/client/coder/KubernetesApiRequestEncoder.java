package com.simplyti.cloud.kube.client.coder;

import java.util.List;

import com.jsoniter.output.JsonStream;
import com.simplyti.cloud.kube.client.reqs.KubernetesApiRequest;

import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import lombok.RequiredArgsConstructor;

@Sharable
@RequiredArgsConstructor
public class KubernetesApiRequestEncoder extends MessageToMessageEncoder<KubernetesApiRequest>{
	
	private final String remoteHost;
	
	@Override
	protected void encode(ChannelHandlerContext ctx, KubernetesApiRequest msg, List<Object> out) throws Exception {
		ByteBuf buffer;
		if(msg.getBody()!=null){
			buffer = ctx.alloc().buffer();
			JsonStream.serialize(msg.getBody(), new ByteBufOutputStream(buffer));
		}else{
			buffer = Unpooled.EMPTY_BUFFER;
		}
		
		FullHttpRequest req = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, msg.getMethod(), msg.getUri(),buffer);
		if(msg.getMethod().equals(HttpMethod.PATCH)){
			req.headers().add(HttpHeaderNames.CONTENT_TYPE,"application/json-patch+json");
		}else if(msg.getBody()!=null){
			req.headers().add(HttpHeaderNames.CONTENT_TYPE,HttpHeaderValues.APPLICATION_JSON);
		}
		req.headers().add(HttpHeaderNames.CONTENT_LENGTH,buffer.readableBytes());
		req.headers().set(HttpHeaderNames.HOST,remoteHost);
		out.add(req);
	}

}
