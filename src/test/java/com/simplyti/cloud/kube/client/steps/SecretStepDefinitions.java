package com.simplyti.cloud.kube.client.steps;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import javax.inject.Inject;

import com.simplyti.cloud.kube.client.KubeClient;
import com.simplyti.cloud.kube.client.domain.Secret;
import com.simplyti.cloud.kube.client.secrets.SecretCreationBuilder;
import com.simplyti.cloud.kube.client.secrets.SecretUpdater;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;

public class SecretStepDefinitions {
	
	@Inject
	private KubeClient client;
	
	@Inject
	private Map<String,Object> scenarioData;
	
	@When("^I create a secret in namespace \"([^\"]*)\" with name \"([^\"]*)\" and properties$")
	public void iCreateASecretInNamespaceWithNameAndProperties(String namespace, String name, Map<String,String> data) throws Throwable {
		SecretCreationBuilder builder = client.namespace(namespace).secrets().builder().withName(name);
		data.forEach((k,v)->builder.withData(k, v));
		Future<Secret> result = builder.create().await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
	}

	@Then("^I check that exist the secret \"([^\"]*)\" with name \"([^\"]*)\" in namespace \"([^\"]*)\"$")
	public void iCheckThatExistTheSecretWithNameInNamespace(String key, String name, String namespace) throws Throwable {
		Future<Secret> result = client.namespace(namespace).secrets().get(name).await();
		assertTrue(result.isSuccess());
		scenarioData.put(key, result.getNow());
	}
	
	@When("^I update the secret \"([^\"]*)\" adding new data:$")
	public void iUpdateTheSecretAddingNewData(String key, Map<String,String> data) throws Throwable {
		Secret secret = (Secret) scenarioData.get(key);
		SecretUpdater updater = client.namespace(secret.getMetadata().getNamespace()).secrets().update(secret.getMetadata().getName());
		data.forEach((k,v)->updater.addData(k, v));
		Future<Secret> result = updater.update().await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
	}
	
	@Then("^I check that secret \"([^\"]*)\" in namespace \"([^\"]*)\" contains data \"([^\"]*)\" equals to \"([^\"]*)\"$")
	public void iCheckThatSecretInNamespaceContainsDataEqualsTo(String name, String namespace, String key, String expected) throws Throwable {
		Secret secret = client.namespace(namespace).secrets().get(name).await().getNow();
		assertThat(secret.getData(),hasKey(key));
		String value = new String(secret.getData().get(key).getData(),CharsetUtil.UTF_8);
		assertThat(value,equalTo(expected));
	}

}
