package com.simplyti.cloud.kube.client;

import com.simplyti.cloud.kube.client.domain.Endpoint;
import com.simplyti.cloud.kube.client.domain.Pod;
import com.simplyti.cloud.kube.client.domain.Secret;
import com.simplyti.cloud.kube.client.domain.Service;
import com.simplyti.cloud.kube.client.domain.ServiceAccount;
import com.simplyti.cloud.kube.client.endpoints.DefaultNamespacedEndpointsApi;
import com.simplyti.cloud.kube.client.endpoints.EndpointCreationBuilder;
import com.simplyti.cloud.kube.client.endpoints.EndpointUpdater;
import com.simplyti.cloud.kube.client.endpoints.NamespacedEndpointsApi;
import com.simplyti.cloud.kube.client.pods.DefaultNamespacedPodsApi;
import com.simplyti.cloud.kube.client.pods.NamespacedPodsApi;
import com.simplyti.cloud.kube.client.pods.PodCreationBuilder;
import com.simplyti.cloud.kube.client.pods.PodUpdater;
import com.simplyti.cloud.kube.client.secrets.DefaultNamespacedSecretsApi;
import com.simplyti.cloud.kube.client.secrets.NamespacedSecretsApi;
import com.simplyti.cloud.kube.client.secrets.SecretCreationBuilder;
import com.simplyti.cloud.kube.client.secrets.SecretUpdater;
import com.simplyti.cloud.kube.client.serviceaccounts.DefaultNamespacedServiceAccountsApi;
import com.simplyti.cloud.kube.client.serviceaccounts.NamespacedServiceAccountsApi;
import com.simplyti.cloud.kube.client.serviceaccounts.ServiceAccountCreationBuilder;
import com.simplyti.cloud.kube.client.serviceaccounts.ServiceAccountUpdater;
import com.simplyti.cloud.kube.client.services.DefaultNamespacedServicesApi;
import com.simplyti.cloud.kube.client.services.NamespacedServicesApi;
import com.simplyti.cloud.kube.client.services.ServiceUpdater;
import com.simplyti.cloud.kube.client.services.DefaultServiceCreationBuilder;

public class NamespacedClient {

	private final InternalClient client;
	private final String namespace;
	private final ResourceSupplier<Service,DefaultServiceCreationBuilder,ServiceUpdater> servicesSupplier;
	private final ResourceSupplier<Pod,PodCreationBuilder,PodUpdater> podsSupplier;
	private final ResourceSupplier<Endpoint,EndpointCreationBuilder,EndpointUpdater> endpointsSupplier;
	private final ResourceSupplier<Secret,SecretCreationBuilder,SecretUpdater> secretsSupplier;
	private final ResourceSupplier<ServiceAccount,ServiceAccountCreationBuilder,ServiceAccountUpdater> serviceAccountsSupplier;

	public NamespacedClient(InternalClient client, String namespace, ResourceSupplier<Service,DefaultServiceCreationBuilder,ServiceUpdater> servicesSupplier,
			ResourceSupplier<Pod,PodCreationBuilder,PodUpdater> podsSupplier, ResourceSupplier<Endpoint,EndpointCreationBuilder,EndpointUpdater> endpointsSupplier,
			ResourceSupplier<Secret,SecretCreationBuilder,SecretUpdater> secretsSupplier, ResourceSupplier<ServiceAccount,ServiceAccountCreationBuilder,ServiceAccountUpdater> serviceAccountsSupplier) {
		this.client=client;
		this.namespace=namespace;
		this.servicesSupplier=servicesSupplier;
		this.podsSupplier=podsSupplier;
		this.endpointsSupplier=endpointsSupplier;
		this.secretsSupplier=secretsSupplier;
		this.serviceAccountsSupplier=serviceAccountsSupplier;
	}

	public NamespacedServicesApi services() {
		return new DefaultNamespacedServicesApi(namespace, servicesSupplier);
	}

	public NamespacedPodsApi pods() {
		return new DefaultNamespacedPodsApi(client,namespace, podsSupplier);
	}

	public NamespacedEndpointsApi endpoints() {
		return new DefaultNamespacedEndpointsApi(namespace, endpointsSupplier);
	}

	public NamespacedSecretsApi secrets() {
		return new DefaultNamespacedSecretsApi(namespace, secretsSupplier);
	}

	public NamespacedServiceAccountsApi serviceaccounts() {
		return new DefaultNamespacedServiceAccountsApi(namespace, serviceAccountsSupplier);
	}

}
