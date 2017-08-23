package com.simplyti.cloud.kube.client.steps;

import static com.jayway.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import com.simplyti.cloud.kube.client.KubeClient;
import com.simplyti.cloud.kube.client.domain.ServiceAccount;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.netty.util.concurrent.Future;

public class ServiceAccountsStepDefinitions {
	
	@Inject
	private KubeClient client;
	
	@Inject
	private Map<String,Object> scenarioData;
	
	@When("^I create a service account in namespace \"([^\"]*)\" with name \"([^\"]*)\"$")
	public void iCreateAServiceAccountInNamespaceWithName(String namespace, String name) throws Throwable {
		Future<ServiceAccount> result = client.namespace(namespace).serviceaccounts().builder()
				.withName(name)
				.create().await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
	}
	
	@Then("^I check that exist a service account \"([^\"]*)\" with name \"([^\"]*)\" in namespace \"([^\"]*)\"$")
	public void iCheckThatExistAServiceAccountWithNameInNamespace(String key, String name, String namespace) throws Throwable {
		scenarioData.put(key, iCheckThatExistAServiceAccountInNamespaceWithName(namespace, name));
	}
	
	@Then("^I check that exist a service account in namespace \"([^\"]*)\" with name \"([^\"]*)\"$")
	public ServiceAccount iCheckThatExistAServiceAccountInNamespaceWithName(String namespace, String name) throws Throwable {
		await().pollInterval(1, TimeUnit.SECONDS).atMost(30,TimeUnit.SECONDS)
		.until(()->client.namespace(namespace).serviceaccounts().get(name).await().getNow(),
				notNullValue());
		
		Future<ServiceAccount> result = client.namespace(namespace).serviceaccounts().get(name).await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
		return result.getNow();
	}
	
	@Then("^I chech that service account \"([^\"]*)\" in namespace \"([^\"]*)\" contains secret \"([^\"]*)\"$")
	public void iChechThatServiceAccountInNamespaceContainsSecret(String name, String namespace, String secret) throws Throwable {
		ServiceAccount serviceAccount = iCheckThatExistAServiceAccountInNamespaceWithName(namespace, name);
		assertThat(serviceAccount.getSecrets(),hasItem(hasProperty("name",equalTo(secret))));
	}
	
	@When("^I update the service acount \"([^\"]*)\" adding new secret \"([^\"]*)\"$")
	public void iUpdateTheServiceAcountAddingNewSecret(String key, String secret) throws Throwable {
		ServiceAccount serviceaccount = (ServiceAccount) scenarioData.get(key);
		Future<ServiceAccount> result = client.namespace(serviceaccount.getMetadata().getNamespace()).serviceaccounts().update(serviceaccount.getMetadata().getName())
			.addSecret(secret)
			.update().await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
	}
	
}
