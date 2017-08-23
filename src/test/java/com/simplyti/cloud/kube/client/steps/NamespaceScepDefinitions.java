package com.simplyti.cloud.kube.client.steps;

import static com.jayway.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import com.google.common.base.Splitter;
import com.simplyti.cloud.kube.client.KubeClient;
import com.simplyti.cloud.kube.client.domain.Namespace;
import com.simplyti.cloud.kube.client.namespaces.NamespaceUpdater;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.netty.util.concurrent.Future;

public class NamespaceScepDefinitions {
	
	@Inject
	private KubeClient client;
	
	@Inject
	private Map<String,Object> scenarioData;
	
	@Then("^I check that exist a namespace with name \"([^\"]*)\"$")
	public void iCheckThatExistANamespaceWithName(String name) throws Throwable {
		await().pollInterval(1, TimeUnit.SECONDS).atMost(30,TimeUnit.SECONDS)
		.until(()->client.namespaces().get(name),
				notNullValue());
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
