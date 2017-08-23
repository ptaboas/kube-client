package com.simplyti.cloud.kube.client.serviceaccounts;

import com.simplyti.cloud.kube.client.AbstractAllKubeApi;
import com.simplyti.cloud.kube.client.InternalClient;
import com.simplyti.cloud.kube.client.domain.ServiceAccount;

public class DefaultServiceAccountsApi extends AbstractAllKubeApi<ServiceAccount,ServiceAccountCreationBuilder,ServiceAccountUpdater,NamespacedServiceAccountsApi> implements ServiceAccountsApi{

	public DefaultServiceAccountsApi(InternalClient client) {
		super(client,"serviceaccounts");
	}

	@Override
	public ServiceAccountCreationBuilder builder(String namespace) {
		return new ServiceAccountCreationBuilder(client,namespace);
	}

	@Override
	public NamespacedServiceAccountsApi namespace(String namespace) {
		return new DefaultNamespacedServiceAccountsApi(namespace,this);
	}

	@Override
	public ServiceAccountUpdater updateBuilder(String namespace, String name) {
		return new ServiceAccountUpdater(client,namespace,name,resourceName,resourceClass,this);
	}

}
