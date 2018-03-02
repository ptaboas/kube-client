package com.simplyti.cloud.kube.client.steps;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.hamcrest.Matchers;

import com.google.common.collect.Iterables;
import com.simplyti.cloud.kube.client.KubeClient;
import com.simplyti.cloud.kube.client.domain.Address;
import com.simplyti.cloud.kube.client.domain.Endpoint;
import com.simplyti.cloud.kube.client.domain.Event;
import com.simplyti.cloud.kube.client.domain.Port;
import com.simplyti.cloud.kube.client.domain.Status;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.netty.util.concurrent.Future;

public class EndpointsStepDefinitions {
	
	@Inject
	private KubeClient client;
	
	@Inject
	private Map<String,Object> scenarioData;
	
	@When("^I observe endpoints storing events in \"([^\"]*)\"$")
	public void iObserveEndpointsStoringEventsIn(String key) throws Throwable {
		String index = client.endpoints().list().await().getNow().getMetadata().getResourceVersion();
		Set<Event<Endpoint>> events = new HashSet<>();
		client.endpoints().watch(index).onEvent(event->{
			events.add(event);
		});
		scenarioData.put(key, events);
	}
	
	@When("^I create an endpoint \"([^\"]*)\" in namespace \"([^\"]*)\" with name \"([^\"]*)\" and address \"([^\"]*)\"$")
	public void iCreateAnEndpointInNamespaceWithNameAndAddress(String key, String namespace, String name, String address) throws Throwable {
		scenarioData.put(key, iCreateAnEndpointInNamespaceWithNameAndAddress(namespace, name, address));
	}
	
	@When("^I create an endpoint in namespace \"([^\"]*)\" with name \"([^\"]*)\" and address \"([^\"]*)\"$")
	public Endpoint iCreateAnEndpointInNamespaceWithNameAndAddress(String namespace, String name, String address) throws Throwable {
		String[] addressParams = address.split(":");
		Future<Endpoint> result = client.namespace(namespace).endpoints().builder()
				.withName(name)
				.withSubset()
					.withAddress(addressParams[0])
					.withPort(Integer.parseInt(addressParams[1]))
					.create()
				.create().await();
		assertTrue(result.isSuccess());
		return result.getNow();
	}
	
	@When("^I delete endpoint \"([^\"]*)\"$")
	public void iDeleteEndpoint(String key) throws Throwable {
		Endpoint endpoint = (Endpoint) scenarioData.get(key);
		Future<Status> result = client.namespace(endpoint.getMetadata().getNamespace()).endpoints().delete(endpoint.getMetadata().getName()).await();
		assertTrue(result.isSuccess());
	}
	
	@Then("^I check that exist an endpoint \"([^\"]*)\" with name \"([^\"]*)\" in namespace \"([^\"]*)\"$")
	public void iCheckThatExistAnEndpointWithNameInNamespace(String key, String name, String namespace) throws Throwable {
		Future<Endpoint> result = client.endpoints().namespace(namespace).get(name).await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
		scenarioData.put(key, result.getNow());
	}
	
	@Then("^I check that endpoint list contains the endpoint \"([^\"]*)\"$")
	public void iCheckThatEndpointListContainsTheEndpoint(String key) throws Throwable {
		Endpoint endpoint = (Endpoint) scenarioData.get(key);
		List<Endpoint> endpoints = client.endpoints().list().await().getNow().getItems();
		assertThat(endpoints,hasItem(hasProperty("metadata",hasProperty("name",equalTo(endpoint.getMetadata().getName())))));
	}
	
	@When("^I update service \"([^\"]*)\" with new address \"([^\"]*)\"$")
	public void iUpdateServiceWithNewAddress(String key, String ip) throws Throwable {
		Endpoint endpoint = (Endpoint) scenarioData.get(key);
		Future<Endpoint> result = client.namespace(endpoint.getMetadata().getNamespace()).endpoints().update(endpoint.getMetadata().getName())
				.addAddress()
					.ip(ip)
					.create()
				.update().await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
	}
	
	@Then("^I check that endpoint \"([^\"]*)\" contains a subset with addresses \"([^\"]*)\" and ports \"([^\"]*)\"$")
	public void iCheckThatEndpointContainsASubsetWithAddressesAndPorts(String key, List<String> addresses, List<String> ports) throws Throwable {
		Endpoint endpoint = (Endpoint) scenarioData.get(key);
		assertThat(endpoint.getSubsets(),hasSize(1));
		assertThat(Iterables.get(endpoint.getSubsets(), 0).getAddresses()
				.stream().map(Address::getIp).collect(Collectors.toList()),
				contains(addresses.stream().map(Matchers::equalTo).collect(Collectors.toList())));
		
		assertThat(Iterables.get(endpoint.getSubsets(), 0).getPorts()
				.stream().map(Port::getPort).collect(Collectors.toList()),
				contains(ports.stream().map(port->Matchers.equalTo(Integer.parseInt(port))).collect(Collectors.toList())));
	}

}
