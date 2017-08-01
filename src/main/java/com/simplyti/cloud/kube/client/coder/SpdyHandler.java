package com.simplyti.cloud.kube.client.coder;

import io.netty.buffer.CompositeByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import com.simplyti.cloud.kube.client.KubeClient;

import io.netty.handler.codec.spdy.SpdyDataFrame;
import io.netty.handler.codec.spdy.SpdyRstStreamFrame;
import io.netty.handler.codec.spdy.SpdyStreamFrame;
import io.netty.handler.codec.spdy.SpdyStreamStatus;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Promise;

public class SpdyHandler extends SimpleChannelInboundHandler<SpdyStreamFrame> {
	
	private static final byte[] EMPTY = new byte[0];
	
	private CompositeByteBuf data;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, SpdyStreamFrame msg) throws Exception {
		if(msg instanceof SpdyDataFrame){
			SpdyDataFrame dataFrame = (SpdyDataFrame) msg;
			if(dataFrame.streamId()==3){
				if(data==null){
					data = ctx.alloc().compositeBuffer();
				}
				data.addComponent(true, dataFrame.content().retain());
			}else if(dataFrame.streamId()==5){
				Promise<byte[]> promise = ctx.channel().attr(AttributeKey.<Promise<byte[]>>valueOf(KubeClient.SINGLE_RESPONSE_PROMISE_NAME)).get();
				promise.setFailure(new RuntimeException(dataFrame.content().toString(CharsetUtil.ISO_8859_1)));
			}
		}else if(msg instanceof SpdyRstStreamFrame){
			SpdyRstStreamFrame rstFrame = (SpdyRstStreamFrame) msg;
			if(rstFrame.status().equals(SpdyStreamStatus.CANCEL)){
				Promise<byte[]> promise = ctx.channel().attr(AttributeKey.<Promise<byte[]>>valueOf(KubeClient.SINGLE_RESPONSE_PROMISE_NAME)).get();
				if(rstFrame.streamId()==3){
					if(data!=null){
						byte[] bytes = new byte[data.readableBytes()];
						data.readBytes(bytes);
						promise.setSuccess(bytes);
						data.release();
					}else{
						promise.setSuccess(EMPTY);
					}
				}
			}
		}
		
	}

}
