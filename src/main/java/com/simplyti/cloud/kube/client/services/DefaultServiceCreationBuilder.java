package com.simplyti.cloud.kube.client.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableMap;
import com.simplyti.cloud.kube.client.AbstractCreationBuilder;
import com.simplyti.cloud.kube.client.InternalClient;
import com.simplyti.cloud.kube.client.domain.Metadata;
import com.simplyti.cloud.kube.client.domain.Service;
import com.simplyti.cloud.kube.client.domain.ServicePort;
import com.simplyti.cloud.kube.client.domain.ServiceSpec;

public class DefaultServiceCreationBuilder extends AbstractCreationBuilder<Service,DefaultServiceCreationBuilder> implements ServiceCreationBuilder<DefaultServiceCreationBuilder>{

	public static final String KIND = "Service";
	public static final String API = "v1";
	
	private final List<ServicePort> servicePorts;
	private final ImmutableMap.Builder<String, String> selectorBuilder = ImmutableMap.<String,String>builder();

	public DefaultServiceCreationBuilder(InternalClient client,String namespace, String resourceName) {
		super(client,namespace,resourceName);
		this.servicePorts=new ArrayList<>();
	}

	@Override
	protected Service create(Metadata metadata) {
		return new Service(KIND, API, 
				metadata, 
				ServiceSpec.builder()
				.ports(servicePorts)
				.selector(selectorBuilder.build())
				.build());
	}
	
	public ServicePortCreationBuilder<DefaultServiceCreationBuilder> withPort(){
		return new ServicePortCreationBuilder<DefaultServiceCreationBuilder>(this);
	}

	public DefaultServiceCreationBuilder addServicePort(ServicePort servicePort) {
		this.servicePorts.add(servicePort);
		return this;
	}

	public DefaultServiceCreationBuilder addSelector(String name, String value) {
		selectorBuilder.put(name,value);
		return this;
	}

	public DefaultServiceCreationBuilder withPorts(Collection<ServicePort> ports) {
		this.servicePorts.addAll(ports);
		return this;
	}

}
