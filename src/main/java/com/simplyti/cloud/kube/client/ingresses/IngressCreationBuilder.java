package com.simplyti.cloud.kube.client.ingresses;

import java.util.ArrayList;
import java.util.List;

import com.simplyti.cloud.kube.client.AbstractCreationBuilder;
import com.simplyti.cloud.kube.client.InternalClient;
import com.simplyti.cloud.kube.client.domain.Ingress;
import com.simplyti.cloud.kube.client.domain.IngressRule;
import com.simplyti.cloud.kube.client.domain.IngressSpec;
import com.simplyti.cloud.kube.client.domain.Metadata;

public class IngressCreationBuilder extends AbstractCreationBuilder<Ingress,IngressCreationBuilder>{
	
	public static final String KIND = "Ingress";
	
	private final List<IngressRule> rules = new ArrayList<>();

	public IngressCreationBuilder(InternalClient client,String api, String namespace, String resoueceName) {
		super(client, false, api, namespace, resoueceName);
	}

	@Override
	protected Ingress create(Metadata metadata) {
		return new Ingress(KIND,api(),metadata,IngressSpec.builder().rules(rules).build());
	}

	public IngressRuleCreationBuilder withRule() {
		return new IngressRuleCreationBuilder(this);
	}

	public IngressCreationBuilder addRule(IngressRule rule) {
		rules.add(rule);
		return this;
	}

}
