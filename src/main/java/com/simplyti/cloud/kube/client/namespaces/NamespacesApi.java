package com.simplyti.cloud.kube.client.namespaces;

import com.simplyti.cloud.kube.client.NamespacedKubeApi;
import com.simplyti.cloud.kube.client.domain.Namespace;

public interface NamespacesApi extends NamespacedKubeApi<Namespace,NamespaceCreationBuilder,NamespaceUpdater>{

}
