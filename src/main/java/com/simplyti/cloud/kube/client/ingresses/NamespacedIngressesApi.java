package com.simplyti.cloud.kube.client.ingresses;

import com.simplyti.cloud.kube.client.NamespacedKubeApi;
import com.simplyti.cloud.kube.client.domain.Ingress;

public interface NamespacedIngressesApi extends NamespacedKubeApi<Ingress, IngressCreationBuilder,IngressUpdater> {

}
