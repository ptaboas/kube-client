package com.simplyti.cloud.kube.client.endpoints;

import com.simplyti.cloud.kube.client.KubeApi;
import com.simplyti.cloud.kube.client.NamespaceAwareKubeApi;
import com.simplyti.cloud.kube.client.domain.Endpoint;

public interface EndpointsApi extends KubeApi<Endpoint>, NamespaceAwareKubeApi<Endpoint,EndpointCreationBuilder,EndpointUpdater,NamespacedEndpointsApi> {

}
