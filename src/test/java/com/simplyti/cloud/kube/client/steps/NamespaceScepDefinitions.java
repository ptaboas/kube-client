package com.simplyti.cloud.kube.client.steps;

import static com.jayway.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import com.google.common.base.Splitter;
import com.simplyti.cloud.kube.client.CreationBuilder;
import com.simplyti.cloud.kube.client.KubeClient;
import com.simplyti.cloud.kube.client.domain.Namespace;
import com.simplyti.cloud.kube.client.namespaces.NamespaceUpdater;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.netty.util.concurrent.Future;

public class NamespaceScepDefinitions {
	
	private static final String CREATED_NAMESPACES = "_CREATED_NAMESPACES_";
	
	@Inject
	private KubeClient client;
	
	@Inject
	private Map<String,Object> scenarioData;
	
	@SuppressWarnings("unchecked")
	@Given("^a namespace \"([^\"]*)\"$")
	public void aNamespace(String name) throws Throwable {
		Future<Namespace> result = client.namespaces().builder().withName(name).create().await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
		await().pollInterval(1, TimeUnit.SECONDS).atMost(30,TimeUnit.SECONDS)
		.until(()->client.serviceaccounts().list().await().get().getItems().size(),
				greaterThan(0));
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
			
			await().pollInterval(1, TimeUnit.SECONDS).atMost(2,TimeUnit.MINUTES)
				.until(()->client.namespaces().list().await().getNow().getItems().stream()
						.noneMatch(namespace->namespaces.contains(namespace.getMetadata().getName())));
		}
	}
	
	@Then("^I check that exist a namespace with name \"([^\"]*)\"$")
	public void iCheckThatExistANamespaceWithName(String name) throws Throwable {
		await().pollInterval(1, TimeUnit.SECONDS).atMost(30,TimeUnit.SECONDS)
		.until(()->client.namespaces().get(name),
				notNullValue());
	}
	
	@SuppressWarnings("unchecked")
	@When("^I create a namespace \"([^\"]*)\" with labes \"([^\"]*)\"$")
	public void iCreateANamespaceWithLabes(String name, String labels) throws Throwable {
		Map<String, String> labelsMap = Splitter.on(',').withKeyValueSeparator('=').split(labels);
		CreationBuilder<Namespace> builder = client.namespaces().builder().withName(name);
		labelsMap.forEach((k,v)->builder.addLabel(k, v));
		Future<Namespace> result = builder.create().await();
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
	
	@When("^I update namespace \"([^\"]*)\" adding new labes \"([^\"]*)\"$")
	public void iUpdateNamespaceAddingNewLabels(String name,String labels) throws Throwable {
		Map<String, String> labelsMap = Splitter.on(',').withKeyValueSeparator('=').split(labels);
		NamespaceUpdater updater = client.namespaces().update(name);
		labelsMap.forEach((k,v)->updater.addLabel(k,v));
		Future<Namespace> result = updater.update().await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
	}
	
	@Then("^I check that exist a namespace \"([^\"]*)\" with name \"([^\"]*)\"$")
	public void iCheckThatExistANamespaceWithName(String key, String name) throws Throwable {
		Future<Namespace> result = client.namespaces().get(name).await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
		scenarioData.put(key, result.getNow());
	}

}
