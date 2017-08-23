package com.simplyti.cloud.kube.client.coder;

import io.netty.buffer.CompositeByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import com.simplyti.cloud.kube.client.InternalClient;
import com.simplyti.cloud.kube.client.exception.KubeClientErrorException;

import io.netty.handler.codec.spdy.SpdyDataFrame;
import io.netty.handler.codec.spdy.SpdyRstStreamFrame;
import io.netty.handler.codec.spdy.SpdyStreamFrame;
import io.netty.handler.codec.spdy.SpdyStreamStatus;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Promise;

public class SpdyHandler extends SimpleChannelInboundHandler<SpdyStreamFrame> {
	
	private static final byte[] EMPTY = new byte[0];
	
	private boolean isFail;
	private CompositeByteBuf std;
	private CompositeByteBuf err;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, SpdyStreamFrame msg) throws Exception {
		if(msg instanceof SpdyDataFrame){
			SpdyDataFrame dataFrame = (SpdyDataFrame) msg;
			if(dataFrame.streamId()==3){
				if(std==null){
					std = ctx.alloc().compositeBuffer();
				}
				std.addComponent(true, dataFrame.content().retain());
			}else if(dataFrame.streamId()==5){
				isFail=true;
				if(err==null){
					err = ctx.alloc().compositeBuffer();
				}
				err.addComponent(true, dataFrame.content().retain());
			}
		}else if(msg instanceof SpdyRstStreamFrame){
			SpdyRstStreamFrame rstFrame = (SpdyRstStreamFrame) msg;
			if(rstFrame.status().equals(SpdyStreamStatus.CANCEL)){
				Promise<byte[]> promise = ctx.channel().attr(AttributeKey.<Promise<byte[]>>valueOf(InternalClient.SINGLE_RESPONSE_PROMISE_NAME)).get();
				if(rstFrame.streamId()==3 && !isFail){
					if(std!=null){
						byte[] bytes = new byte[std.readableBytes()];
						std.readBytes(bytes);
						promise.setSuccess(bytes);
						std.release();
					}else{
						promise.setSuccess(EMPTY);
					}
				}else if(rstFrame.streamId()==5 && isFail){
					if(err!=null){
						promise.setFailure(new KubeClientErrorException("Error executing command: "+err.toString(CharsetUtil.UTF_8).trim()));
						err.release();
					}else{
						promise.setFailure(new KubeClientErrorException("Error executing command"));
					}
				}
			}
		}
		
	}

}
