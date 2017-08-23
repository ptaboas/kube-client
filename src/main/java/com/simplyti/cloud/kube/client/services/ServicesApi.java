package com.simplyti.cloud.kube.client.services;

import com.simplyti.cloud.kube.client.KubeApi;
import com.simplyti.cloud.kube.client.NamespaceAwareKubeApi;
import com.simplyti.cloud.kube.client.domain.Service;

public interface ServicesApi extends KubeApi<Service>, NamespaceAwareKubeApi<Service,DefaultServiceCreationBuilder,ServiceUpdater,NamespacedServicesApi>{


}
