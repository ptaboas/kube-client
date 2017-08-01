package com.simplyti.cloud.kube.client.mapper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.simplyti.cloud.kube.client.domain.Probe;
import com.simplyti.cloud.kube.client.domain.serializer.ProbeDeserializer;

public class KubeObjectMapper extends ObjectMapper{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7171254995819341578L;
	
	public KubeObjectMapper(){
		configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		registerModule(new JavaTimeModule());
		registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
		
		final SimpleModule module = new SimpleModule();
        module.addDeserializer(Probe.class, new ProbeDeserializer());
        registerModule(module);
	}

}
