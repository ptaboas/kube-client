package com.simplyti.cloud.kube.client.steps;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import com.simplyti.cloud.kube.client.KubeClient;
import com.simplyti.cloud.kube.client.domain.Event;
import com.simplyti.cloud.kube.client.domain.Ingress;
import com.simplyti.cloud.kube.client.domain.ResourceList;
import com.simplyti.cloud.kube.client.domain.Status;
import com.simplyti.cloud.kube.client.ingresses.DefaultIngressCreationBuilder;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.netty.util.concurrent.Future;

public class IngressStepDefinitions {
	
	@Inject
	private KubeClient client;
	
	@Inject
	private Map<String,Object> scenarioData;
	
	@When("^I create an ingress in namespace \"([^\"]*)\" with name \"([^\"]*)\", host \"([^\"]*)\" and backend \"([^\"]*)\"$")
	public Ingress iCreateAnIngressInNamespaceWithNameWithHostWithBackend(String namespace, String name, String host, String backend) throws Throwable {
		String[] hostPort = backend.split(":");
		Future<Ingress> result = client.namespace(namespace).ingresses().builder()
			.withName(name)
			.withRule()
				.withHost(host)
				.withPath()
					.backendServiceName(hostPort[0])
					.backendServicePort(Integer.parseInt(hostPort[1]))
					.create()
				.create()
			.create().await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
		return result.getNow();
	}
	
	@When("^I create an ingress \"([^\"]*)\" in namespace \"([^\"]*)\" with name \"([^\"]*)\", host \"([^\"]*)\" and backend \"([^\"]*)\"$")
	public void iCreateAnIngressInNamespaceWithNameHostAndBackend(String key, String namespace, String name, String host, String backend) throws Throwable {
		scenarioData.put(key, iCreateAnIngressInNamespaceWithNameWithHostWithBackend(namespace, name, host, backend));
	}
	
	@When("^I create an ingress in namespace \"([^\"]*)\" with name \"([^\"]*)\", host \"([^\"]*)\", backend \"([^\"]*)\" and annotations$")
	public void iCreateAnIngressInNamespaceWithNameHostBackendAndAnnotations(String namespace, String name, String host, String backend, Map<String,String> annotations) throws Throwable {
		String[] hostPort = backend.split(":");
		DefaultIngressCreationBuilder builder = client.namespace(namespace).ingresses().builder()
			.withName(name);
		annotations.forEach((ann,value)->builder.addAnnotation(ann, value));
		Future<Ingress> result = 	builder.withRule()
				.withHost(host)
				.withPath()
					.backendServiceName(hostPort[0])
					.backendServicePort(Integer.parseInt(hostPort[1]))
					.create()
				.create()
			.create().await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
	}
	
	@When("^I observe ingresses in namespace \"([^\"]*)\" storing events in \"([^\"]*)\"$")
	public void iObserveIngressesInNamespaceStoringEventsIn(String namespace, String key) throws Throwable {
		ResourceList<Ingress> ingresses = client.ingresses().list().await().getNow();
		String index = ingresses.getMetadata().getResourceVersion();
		Set<Event<Ingress>> events = new HashSet<>();
		client.ingresses().namespace(namespace).watch(index).onEvent(event->{
			events.add(event);
		});
		scenarioData.put(key, events);
	}
	
	@When("^I delete ingress \"([^\"]*)\"$")
	public void iDeleteIngress(String key) throws Throwable {
		Ingress ingress = (Ingress) scenarioData.get(key);
		Future<Status> result = client.namespace(ingress.getMetadata().getNamespace()).ingresses().delete(ingress.getMetadata().getName()).await();
		assertTrue(result.isSuccess());
	}
	
	@Then("^I check that exist an ingress \"([^\"]*)\" with name \"([^\"]*)\" in namespace \"([^\"]*)\"$")
	public void iCheckThatExistAnIngressWithNameInNamespace(String key, String name, String namespace) throws Throwable {
		Future<Ingress> result = client.namespace(namespace).ingresses().get(name).await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
		scenarioData.put(key, result.getNow());
	}

}
