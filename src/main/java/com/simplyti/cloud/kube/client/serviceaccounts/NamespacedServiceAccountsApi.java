package com.simplyti.cloud.kube.client.serviceaccounts;

import com.simplyti.cloud.kube.client.NamespacedKubeApi;
import com.simplyti.cloud.kube.client.domain.ServiceAccount;

public interface NamespacedServiceAccountsApi extends NamespacedKubeApi<ServiceAccount, ServiceAccountCreationBuilder, ServiceAccountUpdater>{

}
