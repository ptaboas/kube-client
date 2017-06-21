package com.simplyti.cloud.kube.client;

import static com.jayway.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.simplyti.cloud.kube.client.domain.Endpoint;
import com.simplyti.cloud.kube.client.domain.EndpointList;
import com.simplyti.cloud.kube.client.domain.Event;
import com.simplyti.cloud.kube.client.domain.EventType;
import com.simplyti.cloud.kube.client.observe.Observable;

import io.netty.util.concurrent.Future;

public class EndpointsIT {
	
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
	public void getEndpoints() throws InterruptedException{
		Future<EndpointList> endpoints = client.getEndpoints().await();
		
		assertThat(endpoints.getNow(),notNullValue());
		assertThat(endpoints.getNow().getItems(),hasSize(3));
	}
	
	@Test
	public void observeEndpoints() throws InterruptedException{
		EndpointList endpoints = client.getEndpoints().await().getNow();
		List<Event<Endpoint>> receivedEvents = new ArrayList<>();
		Observable<Endpoint> observable = client.observeEndpoints(endpoints.getMetadata().getResourceVersion()).onEvent(event->{
			receivedEvents.add(event);
		});
		client.createEndpoint("test","nginx",Collections.singleton("192.168.1.1"),Collections.singleton(8080));
		
		await().atMost(2,TimeUnit.SECONDS).until(()->!receivedEvents.isEmpty());
		assertThat(receivedEvents,hasSize(1));
		assertThat(receivedEvents.get(0).getType(),equalTo(EventType.ADDED));
		assertThat(receivedEvents.get(0).getObject().getMetadata(),hasProperty("name",equalTo("nginx")));
		
		assertThat(receivedEvents.get(0).getObject().getSubsets(),hasSize(1));
		assertThat(receivedEvents.get(0).getObject().getSubsets().get(0).getAddresses(),contains(hasProperty("ip",equalTo("192.168.1.1"))));
		assertThat(receivedEvents.get(0).getObject().getSubsets().get(0).getPorts(),contains(hasProperty("port",equalTo(8080))));
		
		observable.close();
	}

}
