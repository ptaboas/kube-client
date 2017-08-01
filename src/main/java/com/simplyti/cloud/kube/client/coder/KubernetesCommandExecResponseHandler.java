package com.simplyti.cloud.kube.client.coder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import static io.netty.handler.codec.spdy.SpdyVersion.SPDY_3_1;

import com.google.common.collect.Iterables;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.spdy.DefaultSpdySynStreamFrame;
import io.netty.handler.codec.spdy.SpdyFrameCodec;
import io.netty.handler.codec.spdy.SpdySessionHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.ReferenceCountUtil;

@Sharable
public class KubernetesCommandExecResponseHandler extends SimpleChannelInboundHandler<HttpResponse> {
	
	
	private boolean verbose;

	public KubernetesCommandExecResponseHandler(boolean verbose) {
		this.verbose=verbose;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpResponse msg) throws Exception {
		if(msg.headers().contains(HttpHeaderNames.UPGRADE)){
			while(Iterables.size(ctx.pipeline())>0){
				ctx.pipeline().removeFirst();
			}
			
			if(verbose){
				ctx.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
			}
			
			ctx.pipeline().addLast(new SpdyFrameCodec(SPDY_3_1));
			ctx.pipeline().addLast(new SpdySessionHandler(SPDY_3_1, false));
			ctx.pipeline().addLast(new SpdyHandler());
			
			DefaultSpdySynStreamFrame errStream = new DefaultSpdySynStreamFrame(1, 0, (byte)0x00);
			errStream.headers().add("streamtype", "stdin");
			ctx.pipeline().writeAndFlush(errStream);
			
			DefaultSpdySynStreamFrame outStream = new DefaultSpdySynStreamFrame(3, 0, (byte)0x00);
			outStream.headers().add("streamtype", "stdout");
			ctx.pipeline().writeAndFlush(outStream);
			
			DefaultSpdySynStreamFrame inStream = new DefaultSpdySynStreamFrame(5, 0, (byte)0x00);
			inStream.headers().add("streamtype", "stderr");
			ctx.pipeline().writeAndFlush(inStream);
		}else{
			ctx.fireChannelRead(ReferenceCountUtil.retain(msg));
		}
	}

}
