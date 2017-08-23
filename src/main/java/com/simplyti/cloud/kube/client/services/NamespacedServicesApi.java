package com.simplyti.cloud.kube.client.services;

import com.simplyti.cloud.kube.client.NamespacedKubeApi;
import com.simplyti.cloud.kube.client.domain.Service;

public interface NamespacedServicesApi extends NamespacedKubeApi<Service, DefaultServiceCreationBuilder, ServiceUpdater>{


}
