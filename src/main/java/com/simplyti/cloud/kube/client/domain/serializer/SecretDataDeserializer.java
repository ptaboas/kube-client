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
		return new SecretData(Base64.getDecoder().decode(jp.getText().getBytes(CharsetUtil.UTF_8)));
	}
    

}
