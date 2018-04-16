package com.simplyti.cloud.kube.client.ingresses;

import com.simplyti.cloud.kube.client.domain.IngressTls;

public class IngressTlsCreationBuilder {

	private final DefaultIngressCreationBuilder builder;
	private String secretName;

	public IngressTlsCreationBuilder(DefaultIngressCreationBuilder builder) {
		this.builder=builder;
	}
	
	public IngressTlsCreationBuilder withSecretName(String secretName) {
		this.secretName=secretName;
		return this;
	}

	public DefaultIngressCreationBuilder create() {
		return builder.addTls(new IngressTls(null, secretName));
	}

}
