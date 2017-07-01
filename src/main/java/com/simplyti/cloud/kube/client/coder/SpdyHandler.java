package com.simplyti.cloud.kube.client.coder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import com.simplyti.cloud.kube.client.KubeClient;

import io.netty.handler.codec.spdy.SpdyDataFrame;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Promise;

public class SpdyHandler extends SimpleChannelInboundHandler<SpdyDataFrame> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, SpdyDataFrame msg) throws Exception {
		Promise<byte[]> promise = ctx.channel().attr(AttributeKey.<Promise<byte[]>>valueOf(KubeClient.SINGLE_RESPONSE_PROMISE_NAME)).get();
		if(msg.streamId()==5){
			byte[] data = new byte[msg.content().readableBytes()];
			msg.content().readBytes(data);
			promise.setSuccess(data);
		}else if(msg.streamId()==1){
			promise.setFailure(new RuntimeException(msg.content().toString(CharsetUtil.ISO_8859_1)));
		}
	}

}
