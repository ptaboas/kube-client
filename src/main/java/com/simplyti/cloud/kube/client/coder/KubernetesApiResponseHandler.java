package com.simplyti.cloud.kube.client.coder;

import java.lang.reflect.ParameterizedType;

import com.jsoniter.JsonIterator;
import com.jsoniter.spi.TypeLiteral;
import com.simplyti.cloud.kube.client.InternalClient;
import com.simplyti.cloud.kube.client.domain.Event;
import com.simplyti.cloud.kube.client.domain.KubernetesResource;
import com.simplyti.cloud.kube.client.domain.KubernetesStatus;
import com.simplyti.cloud.kube.client.exception.KubeClientErrorException;
import com.simplyti.cloud.kube.client.observe.Observable;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Promise;

@Sharable
public class KubernetesApiResponseHandler extends SimpleChannelInboundHandler<Object> {
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		TypeLiteral<?> responseclass = ctx.channel().attr(InternalClient.RESPONSE_CLASS).get();
		ByteBuf content;
		if(msg instanceof FullHttpResponse){
			content = ((FullHttpResponse)msg).content();
		}else{
			content = (ByteBuf) msg;
		}
		
		if(isEventClass(responseclass)){
			Observable<KubernetesResource> observable = ctx.channel().attr(AttributeKey.<Observable<KubernetesResource>>valueOf(InternalClient.OBSERVABLE_RESPONSE_NAME)).get();
			@SuppressWarnings("unchecked")
			Event<KubernetesResource> response = (Event<KubernetesResource>) JsonIterator.deserialize(toBytes(content), responseclass);
			observable.notifyObservers(response);
		}else{
			Promise<Object> future = ctx.channel().attr(AttributeKey.<Promise<Object>>valueOf(InternalClient.SINGLE_RESPONSE_PROMISE_NAME)).get();
			if(responseclass.getType().equals(String.class)){
				future.setSuccess(content.readSlice(content.readableBytes()).toString(CharsetUtil.UTF_8));
			}else{
				HttpResponse httpResponse = (HttpResponse) msg;
				if(httpResponse.status().equals(HttpResponseStatus.NOT_FOUND)){
					future.setSuccess(null);
				}else if (httpResponse.status().code()<300){
					future.setSuccess(JsonIterator.deserialize(toBytes(content), responseclass));
				}else{
					future.setFailure(new KubeClientErrorException(JsonIterator.deserialize(toBytes(content),KubernetesStatus.class), httpResponse.status().code()));
				}
			}
		}
	}

	private byte[] toBytes(ByteBuf content) {
		byte[] data = new byte[content.readableBytes()];
		content.readBytes(data);
		return data;
	}

	private boolean isEventClass(TypeLiteral<?> responseclass) {
		if(responseclass.getType() instanceof ParameterizedType){
			ParameterizedType parameterizedType = (ParameterizedType) responseclass.getType();
			return Event.class.isAssignableFrom((Class<?>) parameterizedType.getRawType());
		}
		return false;
	}
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
		Promise<Object> future = ctx.channel().attr(AttributeKey.<Promise<Object>>valueOf(InternalClient.SINGLE_RESPONSE_PROMISE_NAME)).get();
		future.setFailure(cause);
	}

}
