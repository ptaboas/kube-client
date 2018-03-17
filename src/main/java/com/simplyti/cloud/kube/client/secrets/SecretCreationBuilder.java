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
	
	private final Map<String, SecretData> data = Maps.newHashMap();

	public SecretCreationBuilder(InternalClient client, String api, String namespace, String resourceName) {
		super(client,api,namespace,resourceName);
	}
	
	public SecretCreationBuilder withData(String key, String str){
		this.data.put(key,SecretData.of(str));
		return this;
	}

	@Override
	protected Secret create(Metadata metadata) {
		return new Secret(KIND, api(), metadata, data);
	}

}
