package com.simplyti.cloud.kube.client.mapper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.simplyti.cloud.kube.client.JsonPatch;
import com.simplyti.cloud.kube.client.domain.Probe;
import com.simplyti.cloud.kube.client.domain.SecretData;
import com.simplyti.cloud.kube.client.domain.serializer.ProbeDeserializer;
import com.simplyti.cloud.kube.client.domain.serializer.SecretDataDeserializer;
import com.simplyti.cloud.kube.client.domain.serializer.SecretDataSerializer;

public class KubeObjectMapper extends ObjectMapper{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7171254995819341578L;
	
	public KubeObjectMapper(){
		setSerializationInclusion(Include.NON_NULL);
		configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		registerModule(new JavaTimeModule());
		registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
		
		final SimpleModule module = new SimpleModule();
		module.addSerializer(SecretData.class,new SecretDataSerializer());
		module.addDeserializer(SecretData.class, new SecretDataDeserializer());
        module.addDeserializer(Probe.class, new ProbeDeserializer());
        module.addSerializer(JsonPatch.PatchOperation.class,new PatchOperationSerializer());
        registerModule(module);
	}

}
