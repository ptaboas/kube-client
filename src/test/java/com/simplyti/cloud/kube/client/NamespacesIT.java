package com.simplyti.cloud.kube.client;

import static com.jayway.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.simplyti.cloud.kube.client.domain.Namespace;
import com.simplyti.cloud.kube.client.domain.NamespaceList;
import com.simplyti.cloud.kube.client.domain.Secret;
import com.simplyti.cloud.kube.client.domain.ServiceAccount;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;

public class NamespacesIT {
	
	private KubeClient client;
	private KubeClient setupTest;
	
	@Before
	public void createClient() throws InterruptedException {
		this.setupTest = KubeClient.builder()
				.server("localhost", 8080)
				.verbose(false)
			.build();
		await().atMost(10,TimeUnit.SECONDS).until(()->setupTest.health().await().getNow().equals("ok"));
		await().atMost(10,TimeUnit.SECONDS).until(()->setupTest.getServiceAccount("default","default").await().getNow() != null);
		
		ServiceAccount serviceaAcount = setupTest.getServiceAccount("default", "default").await().getNow();
		Secret secret = setupTest.getSecret("default",serviceaAcount.getSecrets().get(0).getName()).await().getNow();
		String token = Base64.decode(Unpooled.wrappedBuffer(secret.getData().get("token").getBytes(CharsetUtil.UTF_8))).toString(CharsetUtil.UTF_8);
		ByteBuf caCertificate = Base64.decode(Unpooled.wrappedBuffer(secret.getData().get("ca.crt").getBytes(CharsetUtil.UTF_8)));
		
		this.client = KubeClient.builder()
				.server("localhost", 443)
				.secure(new ByteBufInputStream(caCertificate),token)
				.verbose(true)
			.build();
	}
	
	@After
	public void deleteTestNamespace() throws InterruptedException{
		setupTest.deleteNamespace("test").await();
		await().atMost(10,TimeUnit.SECONDS).until(()->setupTest.getNamespaces().await().getNow().getItems().stream().noneMatch(namespace->namespace.getMetadata().getName().equals("test")));
	}
	
	@Test
	public void test() throws InterruptedException{
		Future<Namespace> namespace = client.createNamespace("test").await();
		assertTrue(namespace.isSuccess());
		
		Future<NamespaceList> namespaces = client.getNamespaces().await();
		assertThat(namespaces.getNow().getItems(),hasItem(hasProperty("metadata",hasProperty("name",equalTo("test")))));
	}

}
