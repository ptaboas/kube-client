package com.simplyti.cloud.kube.client.ingresses;

import java.util.ArrayList;
import java.util.List;

import com.simplyti.cloud.kube.client.domain.IngressHttp;
import com.simplyti.cloud.kube.client.domain.IngressPath;
import com.simplyti.cloud.kube.client.domain.IngressRule;

public class IngressRuleCreationBuilder<T extends IngressCreationBuilder<T>> {
	
	private final IngressCreationBuilder<T> builder;
	private final List<IngressPath> paths = new ArrayList<>();
	private String host;

	public IngressRuleCreationBuilder(IngressCreationBuilder<T> builder) {
		this.builder=builder;
	}
	
	public T create() {
		return builder.addRule(IngressRule.builder()
				.host(host)
				.http(IngressHttp.builder().paths(paths).build()).build());
	}

	public IngressRuleCreationBuilder<T> withHost(String host) {
		this.host=host;
		return this;
	}

	public IngressPathCreationBuilder<T> withPath() {
		return withPath(null);
	}
	
	public IngressPathCreationBuilder<T> withPath(String path) {
		return new IngressPathCreationBuilder<>(this,path);
	}

	public IngressRuleCreationBuilder<T> addPath(IngressPath path) {
		this.paths.add(path);
		return this;
	}

}
