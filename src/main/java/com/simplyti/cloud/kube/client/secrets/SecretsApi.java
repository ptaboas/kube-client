package com.simplyti.cloud.kube.client.secrets;

import com.simplyti.cloud.kube.client.KubeApi;
import com.simplyti.cloud.kube.client.NamespaceAwareKubeApi;
import com.simplyti.cloud.kube.client.domain.Secret;

public interface SecretsApi extends KubeApi<Secret>, NamespaceAwareKubeApi<Secret,SecretCreationBuilder,SecretUpdater,NamespacedSecretsApi> {

}
