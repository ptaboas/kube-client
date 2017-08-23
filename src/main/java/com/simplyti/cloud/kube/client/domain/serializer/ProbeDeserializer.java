package com.simplyti.cloud.kube.client.domain.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.simplyti.cloud.kube.client.domain.CommandProbe;
import com.simplyti.cloud.kube.client.domain.HttpProbe;
import com.simplyti.cloud.kube.client.domain.Probe;
import com.simplyti.cloud.kube.client.domain.TcpProbe;

public class ProbeDeserializer extends StdDeserializer<Probe> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4730186727763503062L;

	public ProbeDeserializer() { 
        this(null); 
    } 
 
    public ProbeDeserializer(Class<?> vc) { 
        super(vc); 
    }
    
    @Override
    public Probe deserialize(JsonParser jp, DeserializationContext ctxt) 
      throws IOException, JsonProcessingException {
    	final JsonNode node = jp.getCodec().readTree(jp);
        final ObjectMapper mapper = (ObjectMapper)jp.getCodec();
        if (node.has("exec")) {
            return mapper.treeToValue(node, CommandProbe.class);
        } else if (node.has("httpGet")) {
            return mapper.treeToValue(node, HttpProbe.class);
        }else{
        	return mapper.treeToValue(node, TcpProbe.class);
        }
    }

}
