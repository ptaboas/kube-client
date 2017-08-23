package com.simplyti.cloud.kube.client.domain.serializer;

import java.io.IOException;
import java.util.Base64;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.simplyti.cloud.kube.client.domain.SecretData;

import io.netty.util.CharsetUtil;

public class SecretDataDeserializer extends StdDeserializer<SecretData>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -527306802574946290L;

	public SecretDataDeserializer() { 
        this(null); 
    } 
 
    public SecretDataDeserializer(Class<?> vc) { 
        super(vc); 
    }

	@Override
	public SecretData deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
//		Builder<String, String> builder = ImmutableMap.<String, String>builder();
//		jp.currentToken();
//		while(jp.nextToken() != JsonToken.END_OBJECT){
//			String key = jp.getCurrentName();
//			jp.nextToken();
//			String value = new String(Base64.getDecoder().decode(jp.getText().getBytes(CharsetUtil.UTF_8)),CharsetUtil.UTF_8);
//			builder.put(key,value);
//		}
		return new SecretData(new String(Base64.getDecoder().decode(jp.getText().getBytes(CharsetUtil.UTF_8)),CharsetUtil.UTF_8));
	}
    

}
