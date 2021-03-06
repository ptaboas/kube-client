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
import com.simplyti.cloud.kube.client.domain.ServiceType;

public class DefaultServiceCreationBuilder extends AbstractCreationBuilder<Service,DefaultServiceCreationBuilder> implements ServiceCreationBuilder<DefaultServiceCreationBuilder>{

	public static final String KIND = "Service";
	
	private final List<ServicePort> servicePorts;
	private final ImmutableMap.Builder<String, String> selectorBuilder = ImmutableMap.<String,String>builder();
	private ServiceType type;
	private String clusterIp;

	public DefaultServiceCreationBuilder(InternalClient client, String api, String namespace, String resourceName) {
		super(client,api,namespace,resourceName);
		this.servicePorts=new ArrayList<>();
	}

	@Override
	protected Service create(Metadata metadata) {
		return new Service(KIND, api(), 
				metadata, 
				ServiceSpec.builder()
				.clusterIP(clusterIp)
				.ports(servicePorts)
				.selector(selectorBuilder.build())
				.type(type)
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
	
	public DefaultServiceCreationBuilder withType(ServiceType type) {
		this.type=type;
		return this;
	}

	public DefaultServiceCreationBuilder withClusterIp(String clusterIp) {
		this.clusterIp=clusterIp;
		return this;
	}

}
