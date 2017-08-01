package com.simplyti.cloud.kube.client.steps;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import com.simplyti.cloud.kube.client.KubeClient;
import com.simplyti.cloud.kube.client.domain.Endpoint;
import com.simplyti.cloud.kube.client.domain.Event;

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
		String index = client.getEndpoints().await().getNow().getMetadata().getResourceVersion();
		Set<Event<Endpoint>> events = new HashSet<>();
		client.observeEndpoints(index).onEvent(event->{
			events.add(event);
		});
		scenarioData.put(key, events);
	}
	
	@When("^I create an endpoint \"([^\"]*)\" in namespace \"([^\"]*)\" with name \"([^\"]*)\" and address \"([^\"]*)\"$")
	public void iCreateAnEndpointInNamespaceWithNameAndAddress(String key, String namespace, String name, String address) throws Throwable {
		String[] addressParams = address.split(":");
 		Future<Endpoint> result = client.createEndpoint(namespace, name, Collections.singleton(addressParams[0]), Collections.singleton(Integer.parseInt(addressParams[1]))).await();
 		assertTrue(result.isSuccess());
		scenarioData.put(key, result.getNow());
	}
	
	@When("^I delete endpoint \"([^\"]*)\"$")
	public void iDeleteEndpoint(String key) throws Throwable {
		Endpoint endpoint = (Endpoint) scenarioData.get(key);
		Future<Void> result = client.deleteEndpoint(endpoint.getMetadata().getNamespace(), endpoint.getMetadata().getName()).await();
		assertTrue(result.isSuccess());
	}
	
	@Then("^I check that endpoint list contains the endpoint \"([^\"]*)\"$")
	public void iCheckThatEndpointListContainsTheEndpoint(String key) throws Throwable {
		Endpoint endpoint = (Endpoint) scenarioData.get(key);
		List<Endpoint> endpoints = client.getEndpoints().await().getNow().getItems();
		assertThat(endpoints,hasItem(hasProperty("metadata",hasProperty("name",equalTo(endpoint.getMetadata().getName())))));
	}
	

}
