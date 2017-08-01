package com.simplyti.cloud.kube.client.steps;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import javax.inject.Inject;

import com.simplyti.cloud.kube.client.KubeClient;
import com.simplyti.cloud.kube.client.domain.Secret;
import com.simplyti.cloud.kube.client.domain.SecretList;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.netty.util.concurrent.Future;

public class SecretStepDefinitions {
	
	@Inject
	private KubeClient client;
	
	@Inject
	private Map<String,Object> scenarioData;
	
	@When("^I create a secret in namespace \"([^\"]*)\" with name \"([^\"]*)\" and properties$")
	public void iCreateASecretInNamespaceWithNameAndProperties(String namespace, String name, Map<String,String> data) throws Throwable {
		Future<Secret> result = client.createSecret(namespace,name,data).await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
	}

	@Then("^I check that exist the secret \"([^\"]*)\" with name \"([^\"]*)\" in namespace \"([^\"]*)\"$")
	public void iCheckThatExistTheSecretWithNameInNamespace(String key, String name, String namespace) throws Throwable {
		Future<Secret> result = client.getSecret(namespace, name).await();
		assertTrue(result.isSuccess());
		scenarioData.put(key, result.getNow());
	}

	@Then("^I check that secret list contains secret \"([^\"]*)\"$")
	public void iCheckThatSecretListContainsSecret(String key) throws Throwable {
		Secret secret = (Secret) scenarioData.get(key);
		SecretList secrets = client.getSecrets().await().getNow();
		assertThat(secrets.getItems(),hasItem(hasProperty("metadata",hasProperty("name",equalTo(secret.getMetadata().getName())))));
	}
}
