package com.simplyti.cloud.kube.client.endpoints;

import com.simplyti.cloud.kube.client.domain.Address;

public class EndpointAddressesUpdater {

	private final EndpointUpdater updater;
	private String ip;

	public EndpointAddressesUpdater(EndpointUpdater updater) {
		this.updater=updater;
	}

	public EndpointAddressesUpdater ip(String ip) {
		this.ip=ip;
		return this;
	}
	
	public EndpointUpdater create() {
		return updater.addAddress(0,new Address(ip));
	}

}
