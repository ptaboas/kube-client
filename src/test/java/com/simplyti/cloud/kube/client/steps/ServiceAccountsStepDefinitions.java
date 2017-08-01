package com.simplyti.cloud.kube.client.steps;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


import javax.inject.Inject;

import com.simplyti.cloud.kube.client.KubeClient;
import com.simplyti.cloud.kube.client.domain.ServiceAccount;

import cucumber.api.java.en.Then;
import io.netty.util.concurrent.Future;

public class ServiceAccountsStepDefinitions {
	
	@Inject
	private KubeClient client;
	
	@Then("^I check that exist a service account in namespace \"([^\"]*)\" with name \"([^\"]*)\"$")
	public void iCheckThatExistAServiceAccountInNamespaceWithName(String namespace, String name) throws Throwable {
		Future<ServiceAccount> result = client.getServiceAccount(namespace, name).await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
	}
	
}
