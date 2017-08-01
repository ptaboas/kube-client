package com.simplyti.cloud.kube.client.steps;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Iterables;
import com.simplyti.cloud.kube.client.KubeClient;
import com.simplyti.cloud.kube.client.KubeClientBuilder;
import com.simplyti.cloud.kube.client.domain.Secret;
import com.simplyti.cloud.kube.client.domain.ServiceAccount;
import com.simplyti.cloud.kube.client.domain.ServiceList;

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
		Future<ServiceList> result = client.getServices().await();
		assertTrue(result.isSuccess());
		assertThat(result.getNow(),notNullValue());
	}
	
	@Given("^exist a service account CA certificate in file \"([^\"]*)\"$")
	public void existAServiceAccountCACertificateInFile(String file) throws Throwable {
		ServiceAccount serviceAccount = client.getServiceAccount("default", "default").await().getNow();
		String secretName = Iterables.getFirst(serviceAccount.getSecrets(), null).getName();
		Secret secret = client.getSecret("default", secretName).await().getNow();
		FileUtils.write(BUILD_DIR.resolve(file).toFile(), secret.getData().get("ca.crt"),CharsetUtil.UTF_8);
	}
	
	@Given("^exist a service account token in file \"([^\"]*)\"$")
	public void existAServiceAccountTokenInFile(String file) throws Throwable {
		ServiceAccount serviceAccount = client.getServiceAccount("default", "default").await().getNow();
		String secretName = Iterables.getFirst(serviceAccount.getSecrets(), null).getName();
		Secret secret = client.getSecret("default", secretName).await().getNow();
		FileUtils.write(BUILD_DIR.resolve(file).toFile(), secret.getData().get("token"),CharsetUtil.UTF_8);
	}

}
