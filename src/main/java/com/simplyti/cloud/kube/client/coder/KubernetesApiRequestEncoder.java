package com.simplyti.cloud.kube.client.coder;

import java.io.OutputStream;
import java.util.List;

import com.simplyti.cloud.kube.client.mapper.KubeObjectMapper;
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
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.AttributeKey;
import lombok.AllArgsConstructor;

@Sharable
@AllArgsConstructor
public class KubernetesApiRequestEncoder extends MessageToMessageEncoder<KubernetesApiRequest>{
	
	public static final AttributeKey<Class<?>> RESPONSE_CLASS = AttributeKey.valueOf("responseClass");
	
	private final KubeObjectMapper mapper;

	private final String remoteHost;
	
	public KubernetesApiRequestEncoder(String remoteHost,KubeObjectMapper mapper) {
		this.remoteHost=remoteHost;
		this.mapper=mapper;
	}

	
	@Override
	protected void encode(ChannelHandlerContext ctx, KubernetesApiRequest msg, List<Object> out) throws Exception {
		ctx.channel().attr(RESPONSE_CLASS).set(msg.getResponseClass());
		
		ByteBuf buffer;
		if(msg.getBody()!=null){
			buffer = ctx.alloc().buffer();
			mapper.writeValue((OutputStream)new ByteBufOutputStream(buffer), msg.getBody());
		}else{
			buffer = Unpooled.EMPTY_BUFFER;
		}
		
		FullHttpRequest req = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, msg.getMethod(), msg.getUri(),buffer);
		req.headers().add(HttpHeaderNames.CONTENT_LENGTH,buffer.readableBytes());
		req.headers().set(HttpHeaderNames.HOST,remoteHost);
		out.add(req);
	}

}
