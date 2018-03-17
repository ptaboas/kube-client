package com.simplyti.cloud.kube.client.secrets;

import com.simplyti.cloud.kube.client.AbstractAllKubeApi;
import com.simplyti.cloud.kube.client.InternalClient;
import com.simplyti.cloud.kube.client.domain.Secret;

public class DefaultSecretsApi extends AbstractAllKubeApi<Secret,SecretCreationBuilder,SecretUpdater,NamespacedSecretsApi> implements SecretsApi {

	private static final String API = "v1";
	
	public DefaultSecretsApi(InternalClient client) {
		super(client,API,"secrets");
	}

	@Override
	public SecretCreationBuilder builder(String namespace) {
		return new SecretCreationBuilder(client,API,namespace,"secrets");
	}

	@Override
	public NamespacedSecretsApi namespace(String namespace) {
		return new DefaultNamespacedSecretsApi(namespace,this);
	}

	@Override
	public SecretUpdater updateBuilder(String namespace, String name) {
		return new SecretUpdater(client,API,namespace,name,resourceName,resourceClass,this);
	}

}
