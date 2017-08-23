package com.simplyti.cloud.kube.client.serviceaccounts;

import com.simplyti.cloud.kube.client.AbstractCreationBuilder;
import com.simplyti.cloud.kube.client.InternalClient;
import com.simplyti.cloud.kube.client.domain.Metadata;
import com.simplyti.cloud.kube.client.domain.ServiceAccount;

public class ServiceAccountCreationBuilder extends AbstractCreationBuilder<ServiceAccount,ServiceAccountCreationBuilder>{

	public static final String KIND = "ServiceAccount";
	public static final String API = "v1";
	
	public ServiceAccountCreationBuilder(InternalClient client, String namespace) {
		super(client, namespace, "serviceaccounts");
	}

	@Override
	protected ServiceAccount create(Metadata metadata) {
		return new ServiceAccount(KIND, API, metadata, null);
	}

}
