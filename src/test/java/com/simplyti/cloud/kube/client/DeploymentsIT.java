package com.simplyti.cloud.kube.client;

import static com.jayway.awaitility.Awaitility.await;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.simplyti.cloud.kube.client.domain.Deployment;
import com.simplyti.cloud.kube.client.domain.PodList;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;

public class DeploymentsIT {
	
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
				.eventLoop(eventLoopGroup )
				.server("localhost", 8080)
				.verbose(true)
			.build();
	}
	
	@After
	public void deleteTestNamespace() throws InterruptedException{
		setupTest.deleteNamespace("test").await();
		await().pollInterval(1, TimeUnit.SECONDS).atMost(30,TimeUnit.SECONDS).until(()->setupTest.getNamespaces().await().getNow().getItems().stream().noneMatch(namespace->namespace.getMetadata().getName().equals("test")));
	}
	
	@Test
	public void createDeployment() throws InterruptedException{
		Future<Deployment> result = client.createDeployment("test","test","nginx", Collections.singletonMap("app", "test")).await();
		assertTrue(result.isSuccess());
		
		await().pollInterval(2, TimeUnit.SECONDS).atMost(50,TimeUnit.SECONDS).until(()->client.getpods("test").await().getNow().getItems().size()==1);
		
		PodList pods = client.getpods("test").await().getNow();
		assertThat(pods.getItems(),hasSize(1));
	}
	
}
