package com.simplyti.cloud.kube.client.endpoints;

import java.util.ArrayList;
import java.util.List;

import com.simplyti.cloud.kube.client.AbstractCreationBuilder;
import com.simplyti.cloud.kube.client.InternalClient;
import com.simplyti.cloud.kube.client.domain.Endpoint;
import com.simplyti.cloud.kube.client.domain.Metadata;
import com.simplyti.cloud.kube.client.domain.Subset;

public class EndpointCreationBuilder extends AbstractCreationBuilder<Endpoint,EndpointCreationBuilder> {
	
	public static final String KIND = "Endpoints";
	
	private final List<Subset> subsets = new ArrayList<>();

	public EndpointCreationBuilder(InternalClient client, String api, String namespace, String resourceName) {
		super(client,api,namespace,resourceName);
	}

	@Override
	protected Endpoint create(Metadata metadata) {
		return new Endpoint(KIND,api(),metadata,subsets);
	}

	public EndpointSubsetCreationBuilder withSubset() {
		return new EndpointSubsetCreationBuilder(this);
	}

	public EndpointCreationBuilder withSubset(Subset subset) {
		this.subsets.add(subset);
		return this;
	}

}
