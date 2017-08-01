package com.simplyti.cloud.kube.client.domain.serializer;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import io.netty.util.CharsetUtil;

public class SecretDataSerializer extends StdSerializer<Map<String,String>>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6826059392320609014L;

	public SecretDataSerializer() {
        this(null);
    }
   
    public SecretDataSerializer(Class<Map<String,String>> t) {
        super(t);
    }

	@Override
	public void serialize(Map<String, String> value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException {
		jgen.writeStartObject();
		for(Entry<String, String> entry:value.entrySet()){
			jgen.writeStringField(entry.getKey(), Base64.getEncoder().encodeToString(entry.getValue().getBytes(CharsetUtil.UTF_8)));
		}
		jgen.writeEndObject();
		
	}
    

}
