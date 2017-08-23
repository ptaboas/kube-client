package com.simplyti.cloud.kube.client.serviceaccounts;

import com.simplyti.cloud.kube.client.KubeApi;
import com.simplyti.cloud.kube.client.NamespaceAwareKubeApi;
import com.simplyti.cloud.kube.client.domain.ServiceAccount;

public interface ServiceAccountsApi extends KubeApi<ServiceAccount>, NamespaceAwareKubeApi<ServiceAccount,ServiceAccountCreationBuilder,ServiceAccountUpdater,NamespacedServiceAccountsApi>{

}
