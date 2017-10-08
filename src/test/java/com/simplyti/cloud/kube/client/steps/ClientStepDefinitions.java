package com.simplyti.cloud.kube.client.steps;

import static org.hamcrest.Matchers.notNullValue;
import static com.jayway.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.simplyti.cloud.kube.client.KubeClient;
import com.simplyti.cloud.kube.client.KubeClientBuilder;
import com.simplyti.cloud.kube.client.domain.KubernetesResource;
import com.simplyti.cloud.kube.client.domain.ResourceList;
import com.simplyti.cloud.kube.client.domain.Secret;
import com.simplyti.cloud.kube.client.domain.Service;
import com.simplyti.cloud.kube.client.domain.ServiceAccount;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;

public class ClientStepDefinitions {
	
	private static final Path BUILD_DIR = Paths.get(System.getProperty("user.dir"),"target");
	
	private static final String SERVER = "server";
	private static final String CA_FILE = "caFile";
	private static final String TOKEN_FILE = "tokenFile";
	
	@Inject
	private KubeClient client;
	
	@Inject
	private Map<String,Object> scenarioData;
	
	
	@After
	public void closeClients(){
		scenarioData.values().stream().filter(item->item instanceof KubeClient)
			.map(KubeClient.class::cast).forEach(client->client.close());
	}
	
	@When("^I crete a kubernetes client \"([^\"]*)\" with next options:$")
	public void iCreteAKubernetesClientWithNextOptions(String key, Map<String,String> options) throws Throwable {
	    KubeClientBuilder builder = KubeClient.builder()
	    		.eventLoop(new NioEventLoopGroup())
	    		.verbose(true);
	    builder.server(options.get(SERVER));
	    if(options.containsKey(CA_FILE)){
	    	builder.caFile(BUILD_DIR.resolve(options.get(CA_FILE)).toString());
	    }
	    if(options.containsKey(TOKEN_FILE)){
	    	builder.tokenFile(BUILD_DIR.resolve(options.get(TOKEN_FILE)).toString());
	    }
	    scenarioData.put(key, builder.build());
	}

	@Then("^I check that I get service list successfully using client \"([^\"]*)\"$")
	public void iCheckThatIGetServiceListSuccessfully(String key) throws Throwable {
		KubeClient client = (KubeClient) scenarioData.get(key);
		Future<ResourceList<Service>> result = client.services().list().await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
	}
	
	@When("^I try to retrieve service list \"([^\"]*)\" using client \"([^\"]*)\"$")
	public void iTryToRetrieveServiceListUsingClient(String resultKey, String clientKey) throws Throwable {
		KubeClient client = (KubeClient) scenarioData.get(clientKey);
		Future<ResourceList<Service>> result = client.services().list().await();
		scenarioData.put(resultKey, result);
	}
	
	@Then("^I check that result \"([^\"]*)\" is failure$")
	public void iCheckThatResultIsFailure(String key) throws Throwable {
		Future<?> result = (Future<?>) scenarioData.get(key);
		assertFalse(result.isSuccess());
	}

	@Then("^I check that failure result \"([^\"]*)\" contains message \"([^\"]*)\"$")
	public void iCheckThatFailureResultContainsMessage(String key, String expected) throws Throwable {
		Future<?> result = (Future<?>) scenarioData.get(key);
		assertThat(result.cause().getMessage(),equalTo(expected));
	}
	
	@Given("^exist a service account CA certificate in file \"([^\"]*)\"$")
	public void existAServiceAccountCACertificateInFile(String file) throws Throwable {
		await().atMost(10,TimeUnit.SECONDS).until(()->client.serviceaccounts().namespace("default").get("default").await().getNow()!=null);
		ServiceAccount serviceAccount = client.serviceaccounts().namespace("default").get("default").await().getNow();
		String secretName = Iterables.getFirst(serviceAccount.getSecrets(), null).getName();
		Secret secret = client.secrets().namespace("default").get( secretName).await().getNow();
		FileUtils.write(BUILD_DIR.resolve(file).toFile(), secret.getData().get("ca.crt").getStringValue(),CharsetUtil.UTF_8);
	}
	
	@Given("^exist a service account token in file \"([^\"]*)\"$")
	public void existAServiceAccountTokenInFile(String file) throws Throwable {
		ServiceAccount serviceAccount = client.namespace("default").serviceaccounts().get("default").await().getNow();
		String secretName = Iterables.getFirst(serviceAccount.getSecrets(), null).getName();
		Secret secret = client.secrets().namespace("default").get(secretName).await().getNow();
		FileUtils.write(BUILD_DIR.resolve(file).toFile(), secret.getData().get("token").getStringValue(),CharsetUtil.UTF_8);
	}
	
	@Given("^exist a service account token in file \"([^\"]*)\" with content \"([^\"]*)\"$")
	public void existAServiceAccountTokenInFileWithContent(String file, String content) throws Throwable {
		FileUtils.write(BUILD_DIR.resolve(file).toFile(), content, CharsetUtil.UTF_8);
	}
	
	@Then("^I check that kubernetes rsource \"([^\"]*)\" contains labels \"([^\"]*)\"$")
	public void iCheckThatKubernetesRsourceContainsLabel(String key, String labels) throws Throwable {
		Map<String, String> labelsMap = Splitter.on(',').withKeyValueSeparator('=').split(labels);
		KubernetesResource resource = (KubernetesResource) scenarioData.get(key);
		assertThat(resource.getMetadata().getLabels(),notNullValue());
		assertThat(resource.getMetadata().getLabels().entrySet(),hasSize(labelsMap.size()));
		labelsMap.forEach((k,v)->assertThat(resource.getMetadata().getLabels(),hasEntry(k,v)));
	}

}
