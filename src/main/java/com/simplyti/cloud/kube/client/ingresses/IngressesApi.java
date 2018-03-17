package com.simplyti.cloud.kube.client.ingresses;

import com.simplyti.cloud.kube.client.KubeApi;
import com.simplyti.cloud.kube.client.NamespaceAwareKubeApi;
import com.simplyti.cloud.kube.client.domain.Ingress;

public interface IngressesApi extends KubeApi<Ingress>, NamespaceAwareKubeApi<Ingress,IngressCreationBuilder,IngressUpdater,NamespacedIngressesApi> {

}
