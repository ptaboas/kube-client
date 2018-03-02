package com.simplyti.cloud.kube.client.secrets;

import com.jsoniter.spi.TypeLiteral;
import com.simplyti.cloud.kube.client.AbstractUpdater;
import com.simplyti.cloud.kube.client.InternalClient;
import com.simplyti.cloud.kube.client.JsonPatch;
import com.simplyti.cloud.kube.client.ResourceSupplier;
import com.simplyti.cloud.kube.client.domain.Secret;
import com.simplyti.cloud.kube.client.domain.SecretData;

public class SecretUpdater extends AbstractUpdater<Secret,SecretUpdater>{

	public SecretUpdater(InternalClient client, String namespace, String name, String resourceName,
			TypeLiteral<Secret> resourceClass,ResourceSupplier<Secret, ?,?> supplier) {
		super(client, namespace, name, resourceName, resourceClass,supplier);
	}

	public SecretUpdater addData(String k, String str) {
		addPatch(JsonPatch.add("/data/"+k, SecretData.of(str)));
		return this;
	}

}
