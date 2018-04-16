package com.simplyti.cloud.kube.client.ingresses;

import com.simplyti.cloud.kube.client.domain.IngressRule;

public interface IngressCreationBuilder<T extends IngressCreationBuilder<T>> {

	T addRule(IngressRule build);

}
