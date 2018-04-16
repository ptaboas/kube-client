package com.simplyti.cloud.kube.client.ingresses;

import java.util.ArrayList;
import java.util.List;

import com.simplyti.cloud.kube.client.AbstractCreationBuilder;
import com.simplyti.cloud.kube.client.InternalClient;
import com.simplyti.cloud.kube.client.domain.Ingress;
import com.simplyti.cloud.kube.client.domain.IngressRule;
import com.simplyti.cloud.kube.client.domain.IngressSpec;
import com.simplyti.cloud.kube.client.domain.IngressTls;
import com.simplyti.cloud.kube.client.domain.Metadata;

public class DefaultIngressCreationBuilder extends AbstractCreationBuilder<Ingress,DefaultIngressCreationBuilder> implements IngressCreationBuilder<DefaultIngressCreationBuilder>{
	
	public static final String KIND = "Ingress";
	
	private final List<IngressTls> tlss = new ArrayList<>();
	private final List<IngressRule> rules = new ArrayList<>();

	public DefaultIngressCreationBuilder(InternalClient client,String api, String namespace, String resoueceName) {
		super(client, false, api, namespace, resoueceName);
	}

	@Override
	protected Ingress create(Metadata metadata) {
		return new Ingress(KIND,api(),metadata,IngressSpec.builder().tls(tlss).rules(rules).build());
	}

	public IngressRuleCreationBuilder<DefaultIngressCreationBuilder> withRule() {
		return new IngressRuleCreationBuilder<>(this);
	}
	
	public IngressTlsCreationBuilder withTls() {
		return new IngressTlsCreationBuilder(this);
	}

	public DefaultIngressCreationBuilder addRule(IngressRule rule) {
		rules.add(rule);
		return this;
	}

	public DefaultIngressCreationBuilder addTls(IngressTls tls) {
		tlss.add(tls);
		return this;
	}

}
