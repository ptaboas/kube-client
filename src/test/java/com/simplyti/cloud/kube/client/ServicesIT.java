package com.simplyti.cloud.kube.client;

import static com.jayway.awaitility.Awaitility.await;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.simplyti.cloud.kube.client.domain.Event;
import com.simplyti.cloud.kube.client.domain.EventType;
import com.simplyti.cloud.kube.client.domain.Service;
import com.simplyti.cloud.kube.client.domain.ServiceList;
import com.simplyti.cloud.kube.client.observe.Observable;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.allOf;


import io.netty.util.concurrent.Future;

public class ServicesIT {
	
	private KubeClient client;
	private KubeClient setupTest;
	
	@Before
	public void createClient() throws InterruptedException {
		this.client = KubeClient.builder()
				.server("localhost", 8080)
				.verbose(true)
			.build();
		
		this.setupTest = KubeClient.builder()
				.server("localhost", 8080)
				.verbose(false)
			.build();
		setupTest.createNamespace("test").await();
	}
	
	@After
	public void deleteTestNamespace() throws InterruptedException{
		setupTest.deleteNamespace("test").await();
		await().pollInterval(1, TimeUnit.SECONDS).atMost(10,TimeUnit.SECONDS).until(()->setupTest.getNamespaces().await().getNow().getItems().stream().noneMatch(namespace->namespace.getMetadata().getName().equals("test")));
	}
	
	@Test
	public void createService() throws InterruptedException{
		Future<Service> result = client.createService("test","nginx",ImmutableSet.of(8080,8080),Collections.singletonMap("app", "nginx")).await();
		assertTrue(result.isSuccess());
		
		Future<ServiceList> services = client.getServices().await();
		assertThat(services.getNow().getItems(),hasItem(hasProperty("metadata",allOf(hasProperty("namespace",equalTo("test")),hasProperty("name",equalTo("nginx"))))));
	}
	
	@Test
	public void observeServices() throws InterruptedException{
		ServiceList services = client.getServices().await().getNow();
		List<Event<Service>> receivedEvents = new ArrayList<>();
		Observable<Service> observable = client.observeServices(services.getMetadata().getResourceVersion()).onEvent(event->{
			receivedEvents.add(event);
		});
		client.createService("test","nginx",ImmutableSet.of(8080,8080),Collections.singletonMap("app", "nginx"));
		
		await().atMost(2,TimeUnit.SECONDS).until(()->!receivedEvents.isEmpty());
		assertThat(receivedEvents,hasSize(1));
		assertThat(receivedEvents.get(0).getType(),equalTo(EventType.ADDED));
		assertThat(receivedEvents.get(0).getObject().getMetadata(),hasProperty("name",equalTo("nginx")));
		assertThat(receivedEvents.get(0).getObject().getMetadata(),hasProperty("creationTimestamp",notNullValue()));
		assertThat(receivedEvents.get(0).getObject().getMetadata(),hasProperty("selfLink",notNullValue()));
		assertThat(receivedEvents.get(0).getObject().getMetadata(),hasProperty("uid",notNullValue()));
		assertThat(receivedEvents.get(0).getObject().getSpec().getSelector(),hasEntry("app", "nginx"));
		assertThat(receivedEvents.get(0).getObject().getSpec().getPorts(),contains(hasProperty("port",equalTo(8080))));
		
		observable.close();
	}
	
}
