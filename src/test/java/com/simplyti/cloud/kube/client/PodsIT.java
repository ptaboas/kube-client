package com.simplyti.cloud.kube.client;

import static com.jayway.awaitility.Awaitility.await;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.simplyti.cloud.kube.client.domain.Pod;
import com.simplyti.cloud.kube.client.domain.PodPhase;
import com.simplyti.cloud.kube.client.domain.Probe;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;

public class PodsIT {
	
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
	public void createPod() throws InterruptedException{
		Future<Pod> result = client.createPod("test","test","nginx", Collections.singletonMap("app", "test")).await();
		assertTrue(result.isSuccess());
		
		await().pollInterval(2, TimeUnit.SECONDS).atMost(50,TimeUnit.SECONDS).until(()->client.getPod("test","test").await().getNow().getStatus().getPhase().equals(PodPhase.RUNNING));
	}
	
	@Test
	public void createPodWithReadyness() throws InterruptedException{
		Future<Pod> futurePod = client.createPod("test","test","nginx", Collections.singletonMap("app", "test"),
				Probe.exec(ImmutableSet.of("cat","/tmp/healthy"),1,1,1,1)).await();
		assertTrue(futurePod.isSuccess());
		await().pollInterval(2, TimeUnit.SECONDS).atMost(50,TimeUnit.SECONDS).until(()->client.getPod("test","test").await().getNow().getStatus().getPhase().equals(PodPhase.RUNNING));
		
		Pod pod = client.getPod("test","test").await().getNow();
		assertThat(pod.getStatus().getContainerStatuses().get(0).getReady(),equalTo(false));
		
		client.executeCommand("test","test","touch /tmp/healthy");
		await().pollInterval(2, TimeUnit.SECONDS).atMost(50,TimeUnit.SECONDS).until(()->client.getPod("test","test").await().getNow().getStatus().getContainerStatuses().get(0).getReady()==true);
		pod = client.getPod("test","test").await().getNow();
		assertThat(pod.getStatus().getContainerStatuses().get(0).getReady(),equalTo(true));
	}
	
	
	
	@Test
	public void executeSingleCommand() throws InterruptedException{
		client.createPod("test","test","nginx", Collections.singletonMap("app", "test"));
		await().pollInterval(2, TimeUnit.SECONDS).atMost(50,TimeUnit.SECONDS).until(()->setupTest.getPod("test","test").await().getNow().getStatus().getPhase().equals(PodPhase.RUNNING));
		
		Future<byte[]> result = client.executeCommand("test","test","cat /etc/hostname").await();
		assertTrue(result.isSuccess());
		assertThat(new String(result.getNow()),equalTo("test\n"));
	}
	
}
