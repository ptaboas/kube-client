package com.simplyti.cloud.kube.client.steps;

import static com.jayway.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.simplyti.cloud.kube.client.KubeClient;
import com.simplyti.cloud.kube.client.domain.Pod;
import com.simplyti.cloud.kube.client.domain.PodList;
import com.simplyti.cloud.kube.client.domain.PodPhase;
import com.simplyti.cloud.kube.client.domain.Probe;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.netty.util.concurrent.Future;

public class PodsStepDefinitions {
	
	@Inject
	private KubeClient client;
	
	@Inject
	private Map<String,Object> scenarioData;
	
	@When("^I create a pod in namespace \"([^\"]*)\" with name \"([^\"]*)\" and image \"([^\"]*)\"$")
	public Pod iCreateAPodInNamespaceWithNameandImage(String namespace, String name, String image) throws Throwable {
		Future<Pod> result = client.createPod(namespace,name,image,false).await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
		return result.getNow();
	}
	
	@When("^I create a pod \"([^\"]*)\" in namespace \"([^\"]*)\" with name \"([^\"]*)\" and image \"([^\"]*)\"$")
	public void iCreateAPodInNamespaceWithNameandImage(String key, String namespace, String name, String image) throws Throwable {
		scenarioData.put(key, iCreateAPodInNamespaceWithNameandImage(namespace, name, image));
	}
	
	@When("^I create a pod in namespace \"([^\"]*)\" with name \"([^\"]*)\", image \"([^\"]*)\" and readiness probe \"([^\"]*)\"$")
	public Pod iCreateAPodInNamespaceWithNameImageAndReadinessProbe(String namespace, String name, String image, String readinessProbe) throws Throwable {
		Future<Pod> result = client.createPod(namespace,name,image,Probe.exec(ImmutableSet.copyOf(Splitter.on(' ').split(readinessProbe)),1,1,1,1),false).await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
		return result.getNow();
	}
	
	@Then("^I check that exist a pod \"([^\"]*)\" with name \"([^\"]*)\" in namespace \"([^\"]*)\"$")
	public void iCheckThatExistAPodWithNameInNamespace(String key, String name, String namespace) throws Throwable {
		Future<Pod> result = client.getPod(namespace,name).await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
		scenarioData.put(key, result.getNow());
	}
	
	@Then("^I check that pod \"([^\"]*)\" has \"([^\"]*)\" state$")
	public void iCheckThatServiceHasState(String key, String state) throws Throwable {
		Pod pod = (Pod) scenarioData.get(key);
		await().pollInterval(1, TimeUnit.SECONDS).atMost(30,TimeUnit.SECONDS)
		.until(()->client.getPod(pod.getMetadata().getNamespace(), pod.getMetadata().getName()).await().getNow().getStatus().getPhase(),
				equalTo(PodPhase.RUNNING));
		
	}
	
	@Then("^I check that pod \"([^\"]*)\" is not ready$")
	public void iCheckThatPodIsNotReady(String key) throws Throwable {
		Pod pod = (Pod) scenarioData.get(key);
		await().pollInterval(1, TimeUnit.SECONDS).atMost(30,TimeUnit.SECONDS)
		.until(()->client.getPod(pod.getMetadata().getNamespace(), pod.getMetadata().getName()).await().getNow().getStatus().getContainerStatuses().get(0).getReady(),
				equalTo(false));
	}
	
	@Then("^I check that pod \"([^\"]*)\" is ready$")
	public void iCheckThatPodIsReady(String key) throws Throwable {
		Pod pod = (Pod) scenarioData.get(key);
		await().pollInterval(1, TimeUnit.SECONDS).atMost(30,TimeUnit.SECONDS)
		.until(()->client.getPod(pod.getMetadata().getNamespace(), pod.getMetadata().getName()).await().getNow().getStatus().getContainerStatuses().get(0).getReady(),
				equalTo(true));
	}
	
	@When("^I execute next command \"([^\"]*)\" in pod \"([^\"]*)\"$")
	public byte[] iExecuteNextCommandInPod(String command, String key) throws Throwable {
		Pod pod = (Pod) scenarioData.get(key);
		Future<byte[]> result = client.executeCommand(pod.getMetadata().getNamespace(), pod.getMetadata().getName(), command).await();
		assertTrue(result.isSuccess());
		return result.getNow();
	}
	
	@When("^I execute next command \"([^\"]*)\" in pod \"([^\"]*)\" getting result \"([^\"]*)\"$")
	public void iExecuteNextCommandInPodGettingResult(String command, String key, String result) throws Throwable {
		scenarioData.put(result, iExecuteNextCommandInPod(command, key));
	}
	
	@Then("^I check command result \"([^\"]*)\" contains value \"([^\"]*)\"$")
	public void iCheckCommandResultContainsValue(String key, String expected) throws Throwable {
		byte[] data = (byte[]) scenarioData.get(key);
		String strData = new String(data, "UTF-8").trim();
		assertThat(strData,equalTo(expected));
	}
	
	@Then("^I delete pod \"([^\"]*)\"$")
	public void iDeletePod(String key) throws Throwable {
		Pod pod = (Pod) scenarioData.get(key);
		Future<Void> result = client.deletePod(pod.getMetadata().getNamespace(), pod.getMetadata().getName()).await();
		assertTrue(result.isSuccess());
	}

	@Then("^I check that do not exist the pod \"([^\"]*)\"$")
	public void iCheckThatDoNotExistThePod(String key) throws Throwable {
		Pod pod = (Pod) scenarioData.get(key);
		await().pollInterval(1, TimeUnit.SECONDS).atMost(30,TimeUnit.SECONDS)
		.until(()->client.getPod(pod.getMetadata().getNamespace(), pod.getMetadata().getName()).await().getNow(),
				nullValue());
	}
	
	@When("^I get pods \"([^\"]*)\" in namespace \"([^\"]*)\"$")
	public void iGetPodListThePodList(String key,String namespace) throws Throwable {
		Future<PodList> result = client.getpods(namespace).await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
		scenarioData.put(key, result.getNow());
	}

	@Then("^I check that pod list \"([^\"]*)\" contains pod \"([^\"]*)\"$")
	public void iCheckThatPodListContainsPod(String listKey, String podKey) throws Throwable {
		PodList pods = (PodList) scenarioData.get(listKey);
		Pod pod = (Pod) scenarioData.get(podKey);
		assertThat(pods.getItems(),contains(hasProperty("metadata",hasProperty("name",equalTo(pod.getMetadata().getName())))));
	}

}
