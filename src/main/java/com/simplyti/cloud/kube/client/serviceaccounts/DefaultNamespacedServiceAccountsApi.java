package com.simplyti.cloud.kube.client.serviceaccounts;

import com.simplyti.cloud.kube.client.AbstractNamespacedKubeApi;
import com.simplyti.cloud.kube.client.ResourceSupplier;
import com.simplyti.cloud.kube.client.domain.ServiceAccount;

public class DefaultNamespacedServiceAccountsApi extends AbstractNamespacedKubeApi<ServiceAccount, ServiceAccountCreationBuilder, ServiceAccountUpdater> implements NamespacedServiceAccountsApi {

	public DefaultNamespacedServiceAccountsApi(String namespace,
			ResourceSupplier<ServiceAccount, ServiceAccountCreationBuilder,ServiceAccountUpdater> resourceSupplier) {
		super(namespace, resourceSupplier);
	}

}
