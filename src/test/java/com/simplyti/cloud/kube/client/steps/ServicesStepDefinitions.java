package com.simplyti.cloud.kube.client.steps;

import static com.jayway.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.hamcrest.Matcher;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.simplyti.cloud.kube.client.KubeClient;
import com.simplyti.cloud.kube.client.domain.Event;
import com.simplyti.cloud.kube.client.domain.EventType;
import com.simplyti.cloud.kube.client.domain.KubernetesResource;
import com.simplyti.cloud.kube.client.domain.Namespace;
import com.simplyti.cloud.kube.client.domain.ResourceList;
import com.simplyti.cloud.kube.client.domain.Service;
import com.simplyti.cloud.kube.client.domain.ServicePort;
import com.simplyti.cloud.kube.client.domain.ServiceProtocol;
import com.simplyti.cloud.kube.client.services.DefaultServiceCreationBuilder;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.netty.util.concurrent.Future;

public class ServicesStepDefinitions {
	
	private static final String CREATED_NAMESPACES = "_CREATED_NAMESPACES_";

	@Inject
	private KubeClient client;
	
	@Inject
	private Map<String,Object> scenarioData;
	
	@Before
	public void health(){
		await().atMost(10,TimeUnit.SECONDS).until(()->client.health().await().getNow().equals("ok"));
	}
	
	
	@SuppressWarnings("unchecked")
	@Given("^a namespace \"([^\"]*)\"$")
	public void aNamespace(String name) throws Throwable {
		Future<Namespace> result = client.namespaces().builder().withName(name).create().await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
		if(scenarioData.containsKey(CREATED_NAMESPACES)){
			((List<String>)scenarioData.get(CREATED_NAMESPACES)).add(name);
		}else{
			List<String> namespaces = new ArrayList<>();
			namespaces.add(name);
			scenarioData.put(CREATED_NAMESPACES, namespaces);
		}
	}
	
	@When("^I create a namespace \"([^\"]*)\"$")
	public void iCreateANamespace(String name) throws Throwable {
		aNamespace(name);
	}
	
	@After
	public void deleteNamespaces() throws InterruptedException{
		if(scenarioData.containsKey(CREATED_NAMESPACES)){
			@SuppressWarnings("unchecked")
			List<String> namespaces = ((List<String>)scenarioData.get(CREATED_NAMESPACES));
			namespaces.forEach(client.namespaces()::delete);
			
			await().pollInterval(1, TimeUnit.SECONDS).atMost(30,TimeUnit.SECONDS)
				.until(()->client.namespaces().list().await().getNow().getItems().stream()
						.noneMatch(namespace->namespaces.contains(namespace.getMetadata().getName())));
		}
	}
	
	@When("^I create a service in namespace \"([^\"]*)\" with name \"([^\"]*)\" and port (\\d+)$")
	public Service iCreateAServiceInNamespaceWithNameAndPort(String namespace, String name, int port) throws Throwable {
		Future<Service> result = client.namespace(namespace).services().builder()
				.withName(name)
				.withPort()
					.port(port)
					.create()
				.create().await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
		return result.getNow();
	}
	
	@When("^I create a service in namespace \"([^\"]*)\" with name \"([^\"]*)\" and port \"([^\"]*)\" (\\d+)$")
	public Service iCreateAServiceInNamespaceWithNameNameAndPort(String namespace, String name, String portName, int port) throws Throwable {
		Future<Service> result = client.namespace(namespace).services().builder()
				.withName(name)
				.withPort()
					.name(portName)
					.port(port)
					.create()
				.create().await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
		return result.getNow();
	}
	
	@When("^I create a service \"([^\"]*)\" in namespace \"([^\"]*)\" with name \"([^\"]*)\" and port (\\d+)$")
	public void iCreateAServiceInNamespaceWithNameAndPort(String key, String namespace, String name, int port) throws Throwable {
		scenarioData.put(key, iCreateAServiceInNamespaceWithNameAndPort(namespace, name, port));
	}
	
	@Then("^I check that exist a service \"([^\"]*)\" with name \"([^\"]*)\" in namespace \"([^\"]*)\"$")
	public void iCheckThatExistAServiceWithNameInNamespace(String key, String name, String namespace) throws Throwable {
		Future<Service> result = client.namespace(namespace).services().get(name).await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
		scenarioData.put(key, result.getNow());
	}
	
	@Then("^I check that service \"([^\"]*)\" has next ports:$")
	public void iCheckThatServiceHasNextPorts(String key, List<Map<String,String>> data) throws Throwable {
		Service service = (Service) scenarioData.get(key);
		assertThat(service.getSpec().getPorts(),hasSize(data.size()));
		data.forEach(port->assertThat(service.getSpec().getPorts(),hasItem(allOf(
				portMatch(port)))));
	}

	@SuppressWarnings("unchecked")
	private Matcher<Object>[] portMatch(Map<String, String> data) {
		return data.entrySet().stream()
			.map(entry->{
				Object value;
				if(entry.getKey().equals("port")){
					value = Integer.parseInt(entry.getValue());
				}else if(entry.getKey().equals("protocol")){
					value = ServiceProtocol.valueOf(entry.getValue());
				}else if(entry.getKey().equals("targetPort")){
					value = ServiceProtocol.valueOf(entry.getValue());
				}else{
					value = entry.getValue().chars().allMatch( Character::isDigit )?Integer.parseInt(entry.getValue()):entry.getValue();
				}
				return hasProperty(entry.getKey(),equalTo(value));
			})
			.toArray(size -> new Matcher[size]);
	}
	
	@Then("^I check that service \"([^\"]*)\" does not have selector$")
	public void iCheckThatServiceDoesNotHaveSalector(String key) throws Throwable {
		Service service = (Service) scenarioData.get(key);
		assertThat(service.getSpec().getSelector(),nullValue());
	}

	@When("^I create a service in namespace \"([^\"]*)\" with name \"([^\"]*)\", port (\\d+) and selector \"([^\"]*)\"$")
	public void iCreateAServiceInNamespaceWithNamePortAndSelector(String namespace, String name, int port, String selectors) throws Throwable {
		Map<String, String> selectorsMap = Splitter.on(',').withKeyValueSeparator('=').split(selectors);
		DefaultServiceCreationBuilder builder = client.namespace(namespace).services().builder()
				.withName(name)
				.withPort()
					.port(port)
					.create();
		selectorsMap.forEach((k,v)->builder.addSelector(k, v));
		Future<Service> result = builder.create().await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
	}
	
	@When("^I create a service in namespace \"([^\"]*)\" with name \"([^\"]*)\" and next ports:$")
	public void iCreateAServiceInNamespaceWithNameAndNextPorts(String namespace, String name, List<Map<String,String>> ports) throws Throwable {
		Future<Service> result = client.namespace(namespace).services().builder()
				.withName(name)
				.withPorts(ports(ports))
				.create().await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
	}
	
	private Collection<ServicePort> ports(List<Map<String, String>> ports) {
		return ports.stream().map(portData->{
			return new ServicePort(portData.get("name"), 
					Integer.parseInt(portData.get("port")), 
					portData.containsKey("protocol")?ServiceProtocol.valueOf(portData.get("protocol")):null, 
					(portData.containsKey("targetPort") && portData.get("targetPort").chars().allMatch( Character::isDigit ))?Integer.parseInt(portData.get("targetPort")):portData.get("targetPort"));
		}).collect(Collectors.toList());
	}

	@Then("^I check that service \"([^\"]*)\" contains selectors \"([^\"]*)\"$")
	public void iCheckThatServiceContainsSelectors(String key, String selectors) throws Throwable {
		Map<String, String> selectorsMap = Splitter.on(',').withKeyValueSeparator('=').split(selectors);
		Service service = (Service) scenarioData.get(key);
		assertThat(service.getSpec().getSelector().entrySet(),hasSize(selectorsMap.size()));
		selectorsMap.forEach((k,v)->assertThat(service.getSpec().getSelector(),hasEntry(k,v)));
	}
	
	@When("^I update service \"([^\"]*)\" with new port (\\d+)$")
	public void iUpdateServiceWithNewPort(String key, int port) throws Throwable {
		Service service = (Service) scenarioData.get(key);
		Future<Service> result = client.namespace(service.getMetadata().getNamespace()).services().update(service.getMetadata().getName())
					.setServicePort(Collections.singletonList(ServicePort.port(port)))
					.update().await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
	}
	
	@When("^I update service \"([^\"]*)\" adding new port \"([^\"]*)\" (\\d+)$")
	public void iUpdateServiceAddingNewPort(String key, String portName, int port) throws Throwable {
		Service service = (Service) scenarioData.get(key);
		Future<Service> result = client.namespace(service.getMetadata().getNamespace()).services().update(service.getMetadata().getName())
					.addPort()
						.name(portName)
						.port(port)
						.create()
					.update().await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
	}
	
	@When("^I delete service \"([^\"]*)\"$")
	public void iDeleteService(String key) throws Throwable {
		Service service = (Service) scenarioData.get(key);
		Future<Void> result = client.services().namespace(service.getMetadata().getNamespace()).delete(service.getMetadata().getName()).await();
		assertTrue(result.isSuccess());
	}

	@Then("^I check that do not exist the service \"([^\"]*)\"$")
	public void iCheckThatDoNotExistTheService(String key) throws Throwable {
		Service service = (Service) scenarioData.get(key);
		Future<Service> result = client.namespace(service.getMetadata().getNamespace()).services().get(service.getMetadata().getName()).await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),nullValue());
	}
	
	@When("^I observe services storing events in \"([^\"]*)\"$")
	public void iObserveServicesStoringEventsIn(String key) throws Throwable {
		String index = client.services().list().await().getNow().getMetadata().getResourceVersion();
		Set<Event<Service>> events = Sets.newConcurrentHashSet();
		client.services().watch(index).onEvent(event->{
			events.add(event);
		});
		scenarioData.put(key, events);
	}
	
	@When("^I observe services in namespace \"([^\"]*)\" storing events in \"([^\"]*)\"$")
	public void iObserveServicesInNamespaceStoringEventsIn(String namespace, String key) throws Throwable {
		Set<Event<Service>> events = Sets.newConcurrentHashSet();
		client.services().namespace(namespace).watch().onEvent(event->{
			events.add(event);
		});
		scenarioData.put(key, events);
	}

	
	@Then("^I check that observed events \"([^\"]*)\" contains the \"([^\"]*)\" event of \"([^\"]*)\"$")
	public void iCheckThatObservedEventsContainsTheEventOf(String key, EventType type, String reosurceKey) throws Throwable {
		@SuppressWarnings("unchecked")
		Set<Event<KubernetesResource>> events = (Set<Event<KubernetesResource>>) scenarioData.get(key);
		KubernetesResource resource = (KubernetesResource) scenarioData.get(reosurceKey);
		
		await().pollInterval(1, TimeUnit.SECONDS).atMost(30,TimeUnit.SECONDS)
		.until(()->events.stream().filter(event->event.getObject().getMetadata().getNamespace().equals(resource.getMetadata().getNamespace())).collect(Collectors.toList()),
				hasSize(greaterThanOrEqualTo(1)));
		
		List<Event<KubernetesResource>> filteredEvents = events.stream().filter(event->event.getObject().getMetadata().getNamespace().equals(resource.getMetadata().getNamespace())).collect(Collectors.toList());
		assertThat(filteredEvents,hasSize(1));
		assertThat(filteredEvents,contains(hasProperty("type",equalTo(type))));
		KubernetesResource eventObject = filteredEvents.get(0).getObject();
		assertThat(eventObject.getMetadata().getName(),equalTo(resource.getMetadata().getName()));
	}
	
	@Then("^I check that observed events \"([^\"]*)\" has the \"([^\"]*)\" event of \"([^\"]*)\"$")
	public void iCheckThatObservedEventsHasTheEventOf(String key, EventType type, String reosurceKey) throws Throwable {
		@SuppressWarnings("unchecked")
		Set<Event<KubernetesResource>> events = (Set<Event<KubernetesResource>>) scenarioData.get(key);
		KubernetesResource resource = (KubernetesResource) scenarioData.get(reosurceKey);
		
		await().pollInterval(1, TimeUnit.SECONDS).atMost(30,TimeUnit.SECONDS)
		.until(()->events.stream().filter(event->event.getObject().getMetadata().getNamespace().equals(resource.getMetadata().getNamespace())).collect(Collectors.toList()),
				hasItem(hasProperty("type",equalTo(type))));
		
		List<Event<KubernetesResource>> filteredEvents = events.stream().filter(event->event.getObject().getMetadata().getNamespace().equals(resource.getMetadata().getNamespace())).collect(Collectors.toList());
		assertThat(filteredEvents,hasItem(hasProperty("type",equalTo(type))));
		KubernetesResource eventObject = filteredEvents.get(0).getObject();
		assertThat(eventObject.getMetadata().getName(),equalTo(resource.getMetadata().getName()));
	}
	
	@When("^I clear event list \"([^\"]*)\"$")
	public void iClearEventList(String key) throws Throwable {
		Set<?> events = (Set<?>) scenarioData.get(key);
		events.clear();
	}
	
	@When("^I retrieve all services \"([^\"]*)\" in namespace \"([^\"]*)\"$")
	public void iRetrieveAllServicesInNamespace(String key, String namespace) throws Throwable {
		Future<ResourceList<Service>> result = client.services().namespace(namespace).list().await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
		scenarioData.put(key, result.getNow());
	}

	@Then("^I check that services \"([^\"]*)\" contains the service \"([^\"]*)\"$")
	public void iCheckThatServicesContainsTheService(String servicesKey, String serviceKey) throws Throwable {
		Service service = (Service) scenarioData.get(serviceKey);
		@SuppressWarnings("unchecked")
		ResourceList<Service> services = (ResourceList<Service>) scenarioData.get(servicesKey);
		assertThat(services.getItems(),contains(hasProperty("metadata",hasProperty("name",equalTo(service.getMetadata().getName())))));
	}
	
	@When("^I get services \"([^\"]*)\" in namespace \"([^\"]*)\"$")
	public void iGetServicesInNamespace(String key, String namespace) throws Throwable {
		iRetrieveAllServicesInNamespace(key, namespace);
	}
	
}
