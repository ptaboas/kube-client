package com.simplyti.cloud.kube.client.ingresses;

import com.simplyti.cloud.kube.client.domain.IngressTls;

public class IngressTlsCreationBuilder {

	private final IngressCreationBuilder builder;
	private String secretName;

	public IngressTlsCreationBuilder(IngressCreationBuilder builder) {
		this.builder=builder;
	}
	
	public IngressTlsCreationBuilder withSecretName(String secretName) {
		this.secretName=secretName;
		return this;
	}

	public IngressCreationBuilder create() {
		return builder.addTls(new IngressTls(null, secretName));
	}

}
