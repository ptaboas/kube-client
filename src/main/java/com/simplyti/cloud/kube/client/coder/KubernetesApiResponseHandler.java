package com.simplyti.cloud.kube.client.coder;

import java.io.InputStream;

import com.simplyti.cloud.kube.client.KubeClient;
import com.simplyti.cloud.kube.client.domain.Event;
import com.simplyti.cloud.kube.client.domain.KubernetesResource;
import com.simplyti.cloud.kube.client.mapper.KubeObjectMapper;
import com.simplyti.cloud.kube.client.observe.Observable;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Promise;

@Sharable
public class KubernetesApiResponseHandler extends SimpleChannelInboundHandler<Object> {
	
	private final KubeObjectMapper mapper;
	
	public KubernetesApiResponseHandler(KubeObjectMapper mapper) {
		this.mapper=mapper;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		Class<?> responseclass = ctx.channel().attr(KubernetesApiRequestEncoder.RESPONSE_CLASS).get();
		ByteBuf content;
		if(msg instanceof FullHttpResponse){
			content = ((FullHttpResponse)msg).content();
		}else{
			content = (ByteBuf) msg;
		}
		
		if(Event.class.isAssignableFrom((Class<?>) responseclass)){
			Observable<KubernetesResource> observable = ctx.channel().attr(AttributeKey.<Observable<KubernetesResource>>valueOf(KubeClient.OBSERVABLE_RESPONSE_NAME)).get();
			@SuppressWarnings("unchecked")
			Event<KubernetesResource> response = (Event<KubernetesResource>) mapper.readValue((InputStream)new ByteBufInputStream(content), responseclass);
			observable.notifyObservers(response);
		}else{
			Promise<Object> future = ctx.channel().attr(AttributeKey.<Promise<Object>>valueOf(KubeClient.SINGLE_RESPONSE_PROMISE_NAME)).get();
			if(responseclass.equals(String.class)){
				future.setSuccess(content.readSlice(content.readableBytes()).toString(CharsetUtil.UTF_8));
			}else{
				if(msg instanceof FullHttpResponse){
					if(((FullHttpResponse) msg).status().equals(HttpResponseStatus.NOT_FOUND)){
						future.setSuccess(null);
					}else{
						future.setSuccess(mapper.readValue((InputStream)new ByteBufInputStream(content), responseclass));
					}
				}else{
					future.setSuccess(mapper.readValue((InputStream)new ByteBufInputStream(content), responseclass));
				}
			}
		}
	}

}
