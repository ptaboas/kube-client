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
	public static final String API = "v1";
	
	private final List<Subset> subsets = new ArrayList<>();

	public EndpointCreationBuilder(InternalClient client, String namespace, String resourceName) {
		super(client,namespace,resourceName);
	}

	@Override
	protected Endpoint create(Metadata metadata) {
		return new Endpoint(KIND,API,metadata,subsets);
	}

	public EndpointSubsetCreationBuilder withSubset() {
		return new EndpointSubsetCreationBuilder(this);
	}

	public EndpointCreationBuilder withSubset(Subset subset) {
		this.subsets.add(subset);
		return this;
	}

}
