package com.simplyti.cloud.kube.client.coder;

import java.util.List;

import com.simplyti.cloud.kube.client.domain.Event;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.ReferenceCountUtil;

@Sharable
public class KubeApiEventChunkDecoder extends MessageToMessageDecoder<HttpObject>{

	@Override
	protected void decode(ChannelHandlerContext ctx, HttpObject msg, List<Object> out) throws Exception {
		Class<?> responseclass = ctx.channel().attr(KubernetesApiRequestEncoder.RESPONSE_CLASS).get();
		
		if(Event.class.isAssignableFrom((Class<?>) responseclass)){
			if(HttpResponse.class.isAssignableFrom(msg.getClass())){
				// TODO check status
			}else if(HttpContent.class.isAssignableFrom(msg.getClass())){
				out.add(HttpContent.class.cast(msg).content().retain());
			}else{
				out.add(ReferenceCountUtil.retain(msg));
			}
		}else{
			out.add(ReferenceCountUtil.retain(msg));
		}
		
	}

}
