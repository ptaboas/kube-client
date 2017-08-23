package com.simplyti.cloud.kube.client.secrets;

import com.simplyti.cloud.kube.client.AbstractAllKubeApi;
import com.simplyti.cloud.kube.client.InternalClient;
import com.simplyti.cloud.kube.client.domain.Secret;

public class DefaultSecretsApi extends AbstractAllKubeApi<Secret,SecretCreationBuilder,SecretUpdater,NamespacedSecretsApi> implements SecretsApi {

	public DefaultSecretsApi(InternalClient client) {
		super(client,"secrets");
	}

	@Override
	public SecretCreationBuilder builder(String namespace) {
		return new SecretCreationBuilder(client,namespace,"secrets");
	}

	@Override
	public NamespacedSecretsApi namespace(String namespace) {
		return new DefaultNamespacedSecretsApi(namespace,this);
	}

	@Override
	public SecretUpdater updateBuilder(String namespace, String name) {
		return new SecretUpdater(client,namespace,name,resourceName,resourceClass,this);
	}

}
