package com.simplyti.cloud.kube.client.domain.serializer;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import io.netty.util.CharsetUtil;

public class SecretDataDeserializer extends StdDeserializer<Map<String,String>>{
	
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
	public Map<String, String> deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		Builder<String, String> builder = ImmutableMap.<String, String>builder();
		jp.currentToken();
		while(jp.nextToken() != JsonToken.END_OBJECT){
			String key = jp.getCurrentName();
			jp.nextToken();
			String value = new String(Base64.getDecoder().decode(jp.getText().getBytes(CharsetUtil.UTF_8)),CharsetUtil.UTF_8);
			builder.put(key,value);
		}
		return builder.build();
	}
    

}
