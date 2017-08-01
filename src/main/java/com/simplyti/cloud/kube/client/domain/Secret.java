package com.simplyti.cloud.kube.client.domain;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.simplyti.cloud.kube.client.domain.serializer.SecretDataDeserializer;
import com.simplyti.cloud.kube.client.domain.serializer.SecretDataSerializer;

import lombok.Getter;

@Getter
public class Secret extends KubernetesResource{
	
	public static final String KIND = "Secret";
	public static final String API = "v1";
	
	@JsonSerialize(using=SecretDataSerializer.class)
	@JsonDeserialize(using=SecretDataDeserializer.class)
	private final Map<String, String> data;

	@JsonCreator
	public Secret(
			@JsonProperty("kind") String kind,
			@JsonProperty("apiVersion") String apiVersion,
			@JsonProperty("metadata") Metadata metadata,
			@JsonProperty("data") Map<String,String> data) {
		super(kind,apiVersion,metadata);
		this.data=data;
	}
	
}