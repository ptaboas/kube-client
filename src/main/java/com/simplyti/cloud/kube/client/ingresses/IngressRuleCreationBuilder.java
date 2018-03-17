package com.simplyti.cloud.kube.client.ingresses;

import java.util.ArrayList;
import java.util.List;

import com.simplyti.cloud.kube.client.domain.IngressHttp;
import com.simplyti.cloud.kube.client.domain.IngressPath;
import com.simplyti.cloud.kube.client.domain.IngressRule;

public class IngressRuleCreationBuilder {
	
	private final IngressCreationBuilder builder;
	private final List<IngressPath> paths = new ArrayList<>();
	private String host;

	public IngressRuleCreationBuilder(IngressCreationBuilder builder) {
		this.builder=builder;
	}
	
	public IngressCreationBuilder create() {
		return builder.addRule(IngressRule.builder()
				.host(host)
				.http(IngressHttp.builder().paths(paths).build()).build());
	}

	public IngressRuleCreationBuilder withHost(String host) {
		this.host=host;
		return this;
	}

	public IngressPathCreationBuilder withPath() {
		return withPath(null);
	}
	
	public IngressPathCreationBuilder withPath(String path) {
		return new IngressPathCreationBuilder(this,path);
	}

	public IngressRuleCreationBuilder addPath(IngressPath path) {
		this.paths.add(path);
		return this;
	}

}
