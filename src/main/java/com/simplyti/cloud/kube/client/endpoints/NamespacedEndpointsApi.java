package com.simplyti.cloud.kube.client.endpoints;

import com.simplyti.cloud.kube.client.NamespacedKubeApi;
import com.simplyti.cloud.kube.client.domain.Endpoint;

public interface NamespacedEndpointsApi extends NamespacedKubeApi<Endpoint, EndpointCreationBuilder,EndpointUpdater>{

}
