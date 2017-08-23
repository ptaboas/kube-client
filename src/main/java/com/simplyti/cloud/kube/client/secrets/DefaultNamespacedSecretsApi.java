package com.simplyti.cloud.kube.client.secrets;

import com.simplyti.cloud.kube.client.AbstractNamespacedKubeApi;
import com.simplyti.cloud.kube.client.ResourceSupplier;
import com.simplyti.cloud.kube.client.domain.Secret;

public class DefaultNamespacedSecretsApi extends AbstractNamespacedKubeApi<Secret, SecretCreationBuilder,SecretUpdater> implements NamespacedSecretsApi {

	public DefaultNamespacedSecretsApi(String namespace,
			ResourceSupplier<Secret, SecretCreationBuilder,SecretUpdater> resourceSupplier) {
		super(namespace, resourceSupplier);
	}

}
