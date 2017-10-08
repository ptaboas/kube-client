package com.simplyti.cloud.kube.client.steps;

import static com.jayway.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.simplyti.cloud.kube.client.KubeClient;
import com.simplyti.cloud.kube.client.domain.Event;
import com.simplyti.cloud.kube.client.domain.HttpHeader;
import com.simplyti.cloud.kube.client.domain.HttpProbe;
import com.simplyti.cloud.kube.client.domain.Pod;
import com.simplyti.cloud.kube.client.domain.PodPhase;
import com.simplyti.cloud.kube.client.domain.Probe;
import com.simplyti.cloud.kube.client.domain.ResourceList;
import com.simplyti.cloud.kube.client.domain.TcpProbe;
import com.simplyti.cloud.kube.client.pods.PodCreationBuilder;
import com.simplyti.cloud.kube.client.pods.PodUpdater;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.util.concurrent.Future;

public class PodsStepDefinitions {
	
	@Inject
	private KubeClient client;
	
	@Inject
	private Map<String,Object> scenarioData;
	
	@When("^I create a pod in namespace \"([^\"]*)\" with name \"([^\"]*)\" and image \"([^\"]*)\"$")
	public Pod iCreateAPodInNamespaceWithNameandImage(String namespace, String name, String image) throws Throwable {
		Future<Pod> result = client.namespace(namespace).pods().builder()
				.withName(name)
				.withContainer()
					.withImage(image)
					.create()
				.create().await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
		return result.getNow();
	}
	
	@When("^I create a pod in namespace \"([^\"]*)\" with name \"([^\"]*)\" and image \"([^\"]*)\" and port (\\d+)$")
	public Pod iCreateAPodInNamespaceWithNameandImageAndPort(String namespace, String name, String image,int port) throws Throwable {
		Future<Pod> result = client.namespace(namespace).pods().builder()
				.withName(name)
				.withContainer()
					.withImage(image)
					.withPort()
						.port(port)
						.name("default")
						.create()
					.create()
				.create().await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
		return result.getNow();
	}
	
	@When("^I create a pod in namespace \"([^\"]*)\" with name \"([^\"]*)\", image \"([^\"]*)\", port (\\d+) and labels \"([^\"]*)\"$")
	public void iCreateAPodInNamespaceWithNameImagePortAndLabels(String namespace, String name, String image, int port, String labels) throws Throwable {
		Map<String, String> labelsMap = Splitter.on(',').withKeyValueSeparator('=').split(labels);
		PodCreationBuilder builder = client.namespace(namespace).pods().builder()
				.withName(name);
		labelsMap.forEach((k,v)->builder.addLabel(k, v));
		Future<Pod> result = builder.withContainer()
				.withImage(image)
				.withPort()
					.port(port)
					.name("default")
					.create()
				.create()
			.create().await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
	}

	@When("^I create a pod in namespace \"([^\"]*)\" with name \"([^\"]*)\" and container images \"([^\"]*)\"$")
	public void iCreateAPodInNamespaceWithNameAndContainerImages(String namespace, String name, List<String> images) throws Throwable {
		PodCreationBuilder builder = client.namespace(namespace).pods().builder()
				.withName(name);
		images.forEach(image->{
			builder.withContainer()
				.withImage(image)
				.create();
		});
		Future<Pod> result = builder.create().await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
	}
	
	@When("^I create a pod \"([^\"]*)\" in namespace \"([^\"]*)\" with name \"([^\"]*)\" and image \"([^\"]*)\"$")
	public void iCreateAPodInNamespaceWithNameandImage(String key, String namespace, String name, String image) throws Throwable {
		scenarioData.put(key, iCreateAPodInNamespaceWithNameandImage(namespace, name, image));
	}
	
	@When("^I create a pod in namespace \"([^\"]*)\" with name \"([^\"]*)\", image \"([^\"]*)\" and readiness probe \"([^\"]*)\"$")
	public Pod iCreateAPodInNamespaceWithNameImageAndReadinessProbe(String namespace, String name, String image, String readinessProbe) throws Throwable {
		Future<Pod> result = client.namespace(namespace).pods().builder()
				.withName(name)
				.withContainer()
					.withImage(image)
					.withReadinessProbe(Probe.exec(ImmutableSet.copyOf(Splitter.on(' ').split(readinessProbe)),1,1,1,1))
					.create()
				.create().await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
		return result.getNow();
	}
	
	@When("^I create a pod in namespace \"([^\"]*)\" with name \"([^\"]*)\", image \"([^\"]*)\" and http readiness probe \"([^\"]*)\" in port (\\d+)$")
	public void iCreateAPodInNamespaceWithNameImageAndHttpReadinessProbe(String namespace, String name, String image, String httpPath,int port) throws Throwable {
		Future<Pod> result = client.namespace(namespace).pods().builder()
				.withName(name)
				.withContainer()
					.withImage(image)
					.withReadinessProbe(Probe.http(httpPath,port,1,1,1,1,Collections.singleton(new HttpHeader(HttpHeaderNames.USER_AGENT.toString(),"test"))))
					.create()
				.create().await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
	}
	
	@When("^I create a pod in namespace \"([^\"]*)\" with name \"([^\"]*)\", image \"([^\"]*)\" and tcp readiness probe in port (\\d+)$")
	public void iCreateAPodInNamespaceWithNameImageAndHttpReadinessProbe(String namespace, String name, String image, int port) throws Throwable {
		Future<Pod> result = client.namespace(namespace).pods().builder()
				.withName(name)
				.withContainer()
					.withImage(image)
					.withReadinessProbe(Probe.tcp(port,1,1,1,1))
					.create()
				.create().await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
	}
	
	@When("^I update pod \"([^\"]*)\" adding new label \"([^\"]*)\"$")
	public void iUpdatePodAddingNewLabel(String key, String labels) throws Throwable {
		Map<String, String> labelsMap = Splitter.on(',').withKeyValueSeparator('=').split(labels);
		Pod pod = (Pod) scenarioData.get(key);
		PodUpdater updater = client.namespace(pod.getMetadata().getNamespace()).pods().update(pod.getMetadata().getName());
		labelsMap.forEach((k,v)->updater.addLabel(k,v));
		Future<Pod> result = updater.update().await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
	}
	
	@When("^I update pod \"([^\"]*)\" adding new label \"([^\"]*)\" waiting a while after get the updatter$")
	public void iUpdatePodAddingNewLabelWaitingAWhileAfterGetTheUpdatter(String key, String labels) throws Throwable {
		Map<String, String> labelsMap = Splitter.on(',').withKeyValueSeparator('=').split(labels);
		Pod pod = (Pod) scenarioData.get(key);
		PodUpdater updater = client.namespace(pod.getMetadata().getNamespace()).pods().update(pod.getMetadata().getName());
		Thread.sleep(500);
		labelsMap.forEach((k,v)->updater.addLabel(k,v));
		Future<Pod> result = updater.update().await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
	}
	
	@Then("^I check that exist a pod \"([^\"]*)\" with name \"([^\"]*)\" in namespace \"([^\"]*)\"$")
	public void iCheckThatExistAPodWithNameInNamespace(String key, String name, String namespace) throws Throwable {
		Future<Pod> result = client.namespace(namespace).pods().get(name).await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
		scenarioData.put(key, result.getNow());
	}
	
	@Then("^I check that pod \"([^\"]*)\" has http readiness probe \"([^\"]*)\"$")
	public void iCheckThatPodHasHttpReadinessProbeState(String key, String path) throws Throwable {
		Pod pod = (Pod) scenarioData.get(key);
		assertThat(Iterables.getFirst(pod.getSpec().getContainers(), null).getReadinessProbe(),instanceOf(HttpProbe.class));
		HttpProbe probe = (HttpProbe) Iterables.getFirst(pod.getSpec().getContainers(), null).getReadinessProbe();
		assertThat(probe.getHttpGet().getPath(),equalTo(path));
	}
	
	@Then("^I check that pod \"([^\"]*)\" has tcp readiness probe in port (\\d+)$")
	public void iCheckThatPodHasHttpReadinessProbeState(String key, int port) throws Throwable {
		Pod pod = (Pod) scenarioData.get(key);
		assertThat(Iterables.getFirst(pod.getSpec().getContainers(), null).getReadinessProbe(),instanceOf(TcpProbe.class));
		TcpProbe probe = (TcpProbe) Iterables.getFirst(pod.getSpec().getContainers(), null).getReadinessProbe();
		assertThat(probe.getTcpSocket().getPort(),equalTo(port));
	}
	
	@Then("^I check that pod \"([^\"]*)\" has \"([^\"]*)\" state$")
	public void iCheckThatServiceHasState(String key, String state) throws Throwable {
		Pod pod = (Pod) scenarioData.get(key);
		await().pollInterval(1, TimeUnit.SECONDS).atMost(50,TimeUnit.SECONDS)
		.until(()->client.pods().namespace(pod.getMetadata().getNamespace()).get(pod.getMetadata().getName()).await().getNow().getStatus().getPhase(),
				equalTo(PodPhase.RUNNING));
		scenarioData.put(key, client.pods().namespace(pod.getMetadata().getNamespace()).get(pod.getMetadata().getName()).await().getNow());
	}
	
	@Then("^I check that pod \"([^\"]*)\" is not ready$")
	public void iCheckThatPodIsNotReady(String key) throws Throwable {
		Pod pod = (Pod) scenarioData.get(key);
		await().pollInterval(1, TimeUnit.SECONDS).atMost(30,TimeUnit.SECONDS)
		.until(()->client.pods().namespace(pod.getMetadata().getNamespace()).get(pod.getMetadata().getName()).await().getNow().getStatus().getContainerStatuses().get(0).getReady(),
				equalTo(false));
	}
	
	@Then("^I check that pod \"([^\"]*)\" is ready$")
	public void iCheckThatPodIsReady(String key) throws Throwable {
		Pod pod = (Pod) scenarioData.get(key);
		await().pollInterval(1, TimeUnit.SECONDS).atMost(30,TimeUnit.SECONDS)
		.until(()->client.pods().namespace(pod.getMetadata().getNamespace()).get(pod.getMetadata().getName()).await().getNow().getStatus().getContainerStatuses().get(0).getReady(),
				equalTo(true));
	}
	
	@When("^I execute next command \"([^\"]*)\" in pod \"([^\"]*)\"$")
	public byte[] iExecuteNextCommandInPod(String command, String key) throws Throwable {
		Pod pod = (Pod) scenarioData.get(key);
		Future<byte[]> result = client.pods().namespace(pod.getMetadata().getNamespace()).execute(pod.getMetadata().getName(),command).await();
		assertTrue(result.isSuccess());
		return result.getNow();
	}
	
	@When("^I execute next command \"([^\"]*)\" in pod \"([^\"]*)\" getting result \"([^\"]*)\"$")
	public void iExecuteNextCommandInPodGettingResult(String command, String key, String result) throws Throwable {
		scenarioData.put(result, iExecuteNextCommandInPod(command, key));
	}
	
	@When("^I try to execute next command \"([^\"]*)\" in pod \"([^\"]*)\" getting result \"([^\"]*)\"$")
	public void iTryToExecuteNextCommandInPodGettingResult(String command, String key, String resultKey) throws Throwable {
		Pod pod = (Pod) scenarioData.get(key);
		Future<byte[]> result = client.namespace(pod.getMetadata().getNamespace()).pods().execute(pod.getMetadata().getName(),command).await();
		scenarioData.put(resultKey, result);
	}
	
	@When("^I execute next command \"([^\"]*)\" in container \"([^\"]*)\" of pod \"([^\"]*)\" getting result \"([^\"]*)\"$")
	public void iExecuteNextCommandInContainerOfPodGettingResult(String command, String container, String key, String resultKey) throws Throwable {
		Pod pod = (Pod) scenarioData.get(key);
		Future<byte[]> result = client.namespace(pod.getMetadata().getNamespace()).pods().execute(pod.getMetadata().getName(),container,command).await();
		scenarioData.put(resultKey, result.getNow());
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
		Future<Void> result = client.namespace(pod.getMetadata().getNamespace()).pods().delete(pod.getMetadata().getName()).await();
		assertTrue(result.isSuccess());
	}

	@Then("^I check that do not exist the pod \"([^\"]*)\"$")
	public void iCheckThatDoNotExistThePod(String key) throws Throwable {
		Pod pod = (Pod) scenarioData.get(key);
		await().pollInterval(1, TimeUnit.SECONDS).atMost(30,TimeUnit.SECONDS)
		.until(()->client.namespace(pod.getMetadata().getNamespace()).pods().get(pod.getMetadata().getName()).await().getNow(),
				nullValue());
	}
	
	@When("^I get pods \"([^\"]*)\" in namespace \"([^\"]*)\"$")
	public void iGetPodListThePodList(String key,String namespace) throws Throwable {
		Future<ResourceList<Pod>> result = client.pods().namespace(namespace).list().await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
		scenarioData.put(key, result.getNow());
	}

	@Then("^I check that pod list \"([^\"]*)\" contains pod \"([^\"]*)\"$")
	public void iCheckThatPodListContainsPod(String listKey, String podKey) throws Throwable {
		@SuppressWarnings("unchecked")
		ResourceList<Pod> pods = (ResourceList<Pod>) scenarioData.get(listKey);
		Pod pod = (Pod) scenarioData.get(podKey);
		assertThat(pods.getItems(),contains(hasProperty("metadata",hasProperty("name",equalTo(pod.getMetadata().getName())))));
	}
	
	@When("^I observe pods in namespace \"([^\"]*)\" storing events in \"([^\"]*)\"$")
	public void iObservePodsStoringEventsIn(String namespace, String key) throws Throwable {
		String index = client.pods().list().await().getNow().getMetadata().getResourceVersion();
		Set<Event<Pod>> events = new HashSet<>();
		client.pods().namespace(namespace).watch(index).onEvent(event->{
			events.add(event);
		});
		scenarioData.put(key, events);
	}
	
	@Then("^I check that pod \"([^\"]*)\" contains a not null status host ip$")
	public void iCheckThatPodContainsANotNullStatusHostIp(String key) throws Throwable {
		Pod pod = (Pod) scenarioData.get(key);
		assertThat(pod.getStatus().getHostIP(),notNullValue());
	}
	
	@Then("^I check that pod \"([^\"]*)\" contains a not null status pod ip$")
	public void iCheckThatPodContainsANotNullStatusPodIp(String key) throws Throwable {
		Pod pod = (Pod) scenarioData.get(key);
		assertThat(pod.getStatus().getPodIP(),notNullValue());
	}

}
