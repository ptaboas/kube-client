package com.simplyti.cloud.kube.client;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import cucumber.api.junit.Cucumber;
import io.netty.util.ResourceLeakDetector;

@RunWith(Cucumber.class)
@CucumberOptions(
		features="classpath:features",
		snippets=SnippetType.CAMELCASE,
		plugin="pretty")
public class CucumberITest {
	
	@BeforeClass
	public static void setup() {
		ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);
	}
	
}
