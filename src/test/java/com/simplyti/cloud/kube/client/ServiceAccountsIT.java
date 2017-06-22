package com.simplyti.cloud.kube.client;

import static com.jayway.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.simplyti.cloud.kube.client.domain.ServiceAccount;

import io.netty.util.concurrent.Future;

public class ServiceAccountsIT {
	
	private KubeClient client;
	private KubeClient setupTest;
	
	@Before
	public void createClient() throws InterruptedException {
		this.setupTest = KubeClient.builder()
				.server("localhost", 8080)
				.verbose(false)
			.build();
		await().atMost(10,TimeUnit.SECONDS).until(()->setupTest.health().await().getNow().equals("ok"));
		
		setupTest.createNamespace("test").await();
		
		this.client = KubeClient.builder()
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
	public void test() throws InterruptedException{
		await().atMost(10,TimeUnit.SECONDS).until(()->client.getServiceAccount("test","default").await().getNow() != null);
		
		Future<ServiceAccount> sa = client.getServiceAccount("test","default").await();
		assertTrue(sa.isSuccess());
		
		assertThat(sa.getNow().getMetadata().getName(),equalTo("default"));
	}

}
