package com.simplyti.cloud.kube.client.serviceaccounts;

import com.simplyti.cloud.kube.client.AbstractCreationBuilder;
import com.simplyti.cloud.kube.client.InternalClient;
import com.simplyti.cloud.kube.client.domain.Metadata;
import com.simplyti.cloud.kube.client.domain.ServiceAccount;

public class ServiceAccountCreationBuilder extends AbstractCreationBuilder<ServiceAccount,ServiceAccountCreationBuilder>{

	public static final String KIND = "ServiceAccount";
	
	public ServiceAccountCreationBuilder(InternalClient client, String api, String namespace) {
		super(client, api, namespace, "serviceaccounts");
	}

	@Override
	protected ServiceAccount create(Metadata metadata) {
		return new ServiceAccount(KIND, api(), metadata, null);
	}

}
