package com.simplyti.cloud.kube.client.ingresses;

import java.util.ArrayList;
import java.util.List;

import com.simplyti.cloud.kube.client.domain.IngressTls;

public class IngressTlsCreationBuilder {

	private final DefaultIngressCreationBuilder builder;
	private String secretName;
	private List<String> hosts = new ArrayList<>();

	public IngressTlsCreationBuilder(DefaultIngressCreationBuilder builder) {
		this.builder=builder;
	}
	
	public IngressTlsCreationBuilder withSecretName(String secretName) {
		this.secretName=secretName;
		return this;
	}
	
	public IngressTlsCreationBuilder withHost(String host) {
		hosts.add(host);
		return this;
	}

	public DefaultIngressCreationBuilder create() {
		return builder.addTls(new IngressTls(hosts, secretName));
	}

}
