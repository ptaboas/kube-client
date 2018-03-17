package com.simplyti.cloud.kube.client.serviceaccounts;

import com.jsoniter.spi.TypeLiteral;
import com.simplyti.cloud.kube.client.AbstractUpdater;
import com.simplyti.cloud.kube.client.InternalClient;
import com.simplyti.cloud.kube.client.JsonPatch;
import com.simplyti.cloud.kube.client.ResourceSupplier;
import com.simplyti.cloud.kube.client.domain.ObjectReference;
import com.simplyti.cloud.kube.client.domain.ServiceAccount;

public class ServiceAccountUpdater extends AbstractUpdater<ServiceAccount,ServiceAccountUpdater>{

	public ServiceAccountUpdater(InternalClient client, String api,
			String namespace, String name, String resourceName, TypeLiteral<ServiceAccount> resourceClass,
			ResourceSupplier<ServiceAccount, ?,?> supplier) {
		super(client, api, namespace, name, resourceName, resourceClass,supplier);
	}

	public ServiceAccountUpdater addSecret(String secret) {
		addPatch(JsonPatch.add("/secrets/-", new ObjectReference(secret)));
		return this;
	}

}
