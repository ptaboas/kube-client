package com.simplyti.cloud.kube.client.secrets;

import java.util.Map;

import com.google.common.collect.Maps;
import com.simplyti.cloud.kube.client.AbstractCreationBuilder;
import com.simplyti.cloud.kube.client.InternalClient;
import com.simplyti.cloud.kube.client.domain.Metadata;
import com.simplyti.cloud.kube.client.domain.Secret;
import com.simplyti.cloud.kube.client.domain.SecretData;

public class SecretCreationBuilder extends AbstractCreationBuilder<Secret,SecretCreationBuilder>{
	
	public static final String KIND = "Secret";
	public static final String API = "v1";
	
	private final Map<String, SecretData> data = Maps.newHashMap();

	public SecretCreationBuilder(InternalClient client, String namespace, String resourceName) {
		super(client,namespace,resourceName);
	}
	
	public SecretCreationBuilder withData(String key, SecretData data){
		this.data.put(key,data);
		return this;
	}

	@Override
	protected Secret create(Metadata metadata) {
		return new Secret(KIND, API, metadata, data);
	}

}
