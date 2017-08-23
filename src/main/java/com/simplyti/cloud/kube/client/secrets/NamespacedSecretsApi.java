package com.simplyti.cloud.kube.client.secrets;

import com.simplyti.cloud.kube.client.NamespacedKubeApi;
import com.simplyti.cloud.kube.client.domain.Secret;

public interface NamespacedSecretsApi extends NamespacedKubeApi<Secret, SecretCreationBuilder,SecretUpdater>{

}
