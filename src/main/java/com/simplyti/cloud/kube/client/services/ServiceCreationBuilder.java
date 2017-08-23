package com.simplyti.cloud.kube.client.services;

import com.simplyti.cloud.kube.client.domain.ServicePort;

public interface ServiceCreationBuilder<T extends ServiceCreationBuilder<T>> {

	T addServicePort(ServicePort build);

}
