package com.simplyti.cloud.kube.client.ingresses;

import com.jsoniter.spi.TypeLiteral;
import com.simplyti.cloud.kube.client.AbstractUpdater;
import com.simplyti.cloud.kube.client.InternalClient;
import com.simplyti.cloud.kube.client.JsonPatch;
import com.simplyti.cloud.kube.client.ResourceSupplier;
import com.simplyti.cloud.kube.client.domain.Ingress;
import com.simplyti.cloud.kube.client.domain.IngressRule;

public class IngressUpdater extends AbstractUpdater<Ingress,IngressUpdater> implements IngressCreationBuilder<IngressUpdater>{

	public IngressUpdater(InternalClient client, boolean core, String api, String namespace, String name, String resourceName,
			TypeLiteral<Ingress> resourceClass, ResourceSupplier<Ingress, ?, ?> supplier) {
		super(client, core,api,namespace, name, resourceName, resourceClass, supplier);
	}

	public IngressRuleCreationBuilder<IngressUpdater> addRule() {
		return new IngressRuleCreationBuilder<>(this);
	}

	@Override
	public IngressUpdater addRule(IngressRule rule) {
		this.addPatch(JsonPatch.add("/spec/rules/-",rule));
		return this;
	}

}
