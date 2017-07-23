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
import com.google.common.collect.Iterables;
import com.simplyti.cloud.kube.client.domain.Event;
import com.simplyti.cloud.kube.client.domain.EventType;
import com.simplyti.cloud.kube.client.domain.Metadata;
import com.simplyti.cloud.kube.client.domain.ServiceProtocol;
import com.simplyti.cloud.kube.client.domain.Service;
import com.simplyti.cloud.kube.client.domain.ServiceList;
import com.simplyti.cloud.kube.client.domain.ServicePort;
import com.simplyti.cloud.kube.client.domain.ServiceSpec;
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
import static org.hamcrest.Matchers.empty;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;

public class ServicesIT {
	
	private KubeClient client;
	private KubeClient setupTest;
	
	@Before
	public void createClient() throws InterruptedException {
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup(1);
		this.setupTest = KubeClient.builder()
				.eventLoop(eventLoopGroup)
				.server("localhost", 8080)
				.verbose(false)
			.build();
		await().atMost(10,TimeUnit.SECONDS).until(()->setupTest.health().await().getNow().equals("ok"));
		
		setupTest.createNamespace("test").await();
		
		this.client = KubeClient.builder()
				.eventLoop(eventLoopGroup)
				.server("localhost", 8080)
				.verbose(true)
			.build();
	}
	
	@After
	public void deleteTestNamespace() throws InterruptedException{
		setupTest.deleteNamespace("test").await();
		await().pollInterval(1, TimeUnit.SECONDS).atMost(10,TimeUnit.SECONDS).until(()->setupTest.getNamespaces().await().getNow().getItems().stream().noneMatch(namespace->namespace.getMetadata().getName().equals("test")));
	}
	
	@Test
	public void createService() throws InterruptedException{
		Future<Service> result = client.createService("test","nginx",Collections.singleton(ServicePort.port(80)),Collections.singletonMap("app", "nginx")).await();
		assertTrue(result.isSuccess());
		
		Future<ServiceList> services = client.getServices().await();
		assertThat(services.getNow().getItems(),hasItem(
				hasProperty("metadata",allOf(
						hasProperty("namespace",equalTo("test")),
						hasProperty("name",equalTo("nginx"))))));
	}
	
	@Test
	public void createServiceWithMultiplePorts() throws InterruptedException{
		Future<Service> result = client.createService("test","nginx",ImmutableSet.of(ServicePort.port(80,"tcp"),ServicePort.port(80,"udp",ServiceProtocol.UDP)),Collections.singletonMap("app", "nginx")).await();
		assertTrue(result.isSuccess());
		
		Future<ServiceList> services = client.getServices().await();
		assertThat(services.getNow().getItems(),hasItem(
				hasProperty("metadata",allOf(
						hasProperty("namespace",equalTo("test")),
						hasProperty("name",equalTo("nginx"))))));
		
		Service service = Iterables.find(services.getNow().getItems(), item->item.getMetadata().getName().equals("nginx"));
		assertThat(service.getSpec().getPorts(),hasSize(2));
	}
	
	@Test
	public void updateService() throws InterruptedException{
		Future<Service> result = client.createService("test","nginx",Collections.singleton(ServicePort.port(80)),Collections.singletonMap("app", "nginx")).await();
		assertTrue(result.isSuccess());
		
		Future<ServiceList> services = client.getServices("test").await();
		assertThat(services.getNow().getItems(),hasSize(1));
		Service service = services.getNow().getItems().get(0);
		assertThat(service.getSpec().getPorts(),hasSize(1));
		assertThat(service.getSpec().getPorts().get(0).getPort(),equalTo(80));
		
		Service newService = new Service(service.getKind(), service.getApiVersion(), 
				new Metadata(service.getMetadata().getName(), null, service.getMetadata().getNamespace(), null, null, service.getMetadata().getResourceVersion(), null, service.getMetadata().getLabels(), service.getMetadata().getAnnotations()), 
				new ServiceSpec(service.getSpec().getClusterIP(), Collections.singletonList(ServicePort.port(443)), service.getSpec().getSelector()));
		
		Future<Service> updateResult = client.updateService(newService).await();
		assertTrue(updateResult.isSuccess());
		
		services = client.getServices("test").await();
		assertThat(services.getNow().getItems(),hasSize(1));
		service = services.getNow().getItems().get(0);
		assertThat(service.getSpec().getPorts(),hasSize(1));
		assertThat(service.getSpec().getPorts().get(0).getPort(),equalTo(443));
		
	}
	
	@Test
	public void deleteService() throws InterruptedException{
		Future<Service> result = client.createService("test","nginx",Collections.singleton(ServicePort.port(80)),Collections.singletonMap("app", "nginx")).await();
		assertTrue(result.isSuccess());
		
		Future<ServiceList> services = client.getServices("test").await();
		assertThat(services.getNow().getItems(),contains(hasProperty("metadata",allOf(hasProperty("namespace",equalTo("test")),hasProperty("name",equalTo("nginx"))))));
	
		Future<Void> deleteResult = client.deleteService("test", "nginx").await();
		assertTrue(deleteResult.isSuccess());
		
		services = client.getServices("test").await();
		assertThat(services.getNow().getItems(),empty());
		
	}
	
	@Test
	public void observeServices() throws InterruptedException{
		ServiceList services = client.getServices().await().getNow();
		List<Event<Service>> receivedEvents = new ArrayList<>();
		Observable<Service> observable = client.observeServices(services.getMetadata().getResourceVersion()).onEvent(event->{
			receivedEvents.add(event);
		});
		client.createService("test","nginx",Collections.singleton(ServicePort.port(80)),Collections.singletonMap("app", "nginx"));
		
		await().atMost(2,TimeUnit.SECONDS).until(()->!receivedEvents.isEmpty());
		assertThat(receivedEvents,hasSize(1));
		assertThat(receivedEvents.get(0).getType(),equalTo(EventType.ADDED));
		assertThat(receivedEvents.get(0).getObject().getMetadata(),hasProperty("name",equalTo("nginx")));
		assertThat(receivedEvents.get(0).getObject().getMetadata(),hasProperty("creationTimestamp",notNullValue()));
		assertThat(receivedEvents.get(0).getObject().getMetadata(),hasProperty("selfLink",notNullValue()));
		assertThat(receivedEvents.get(0).getObject().getMetadata(),hasProperty("uid",notNullValue()));
		assertThat(receivedEvents.get(0).getObject().getSpec().getSelector(),hasEntry("app", "nginx"));
		assertThat(receivedEvents.get(0).getObject().getSpec().getPorts(),contains(hasProperty("port",equalTo(80))));
		
		observable.close();
	}
	
}
