package com.simplyti.cloud.kube.client.serviceaccounts;

import com.simplyti.cloud.kube.client.AbstractAllKubeApi;
import com.simplyti.cloud.kube.client.InternalClient;
import com.simplyti.cloud.kube.client.domain.ServiceAccount;

public class DefaultServiceAccountsApi extends AbstractAllKubeApi<ServiceAccount,ServiceAccountCreationBuilder,ServiceAccountUpdater,NamespacedServiceAccountsApi> implements ServiceAccountsApi{

	private static final String API = "v1";
	
	public DefaultServiceAccountsApi(InternalClient client) {
		super(client,API,"serviceaccounts");
	}

	@Override
	public ServiceAccountCreationBuilder builder(String namespace) {
		return new ServiceAccountCreationBuilder(client,API,namespace);
	}

	@Override
	public NamespacedServiceAccountsApi namespace(String namespace) {
		return new DefaultNamespacedServiceAccountsApi(namespace,this);
	}

	@Override
	public ServiceAccountUpdater updateBuilder(String namespace, String name) {
		return new ServiceAccountUpdater(client,API,namespace,name,resourceName,resourceClass,this);
	}

}
