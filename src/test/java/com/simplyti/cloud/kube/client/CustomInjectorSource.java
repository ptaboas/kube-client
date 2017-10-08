package com.simplyti.cloud.kube.client;

import java.util.Map;

import com.google.common.collect.Maps;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;

import cucumber.runtime.java.guice.InjectorSource;
import cucumber.runtime.java.guice.ScenarioScoped;
import cucumber.runtime.java.guice.impl.ScenarioModule;
import cucumber.runtime.java.guice.impl.SequentialScenarioScope;
import io.netty.channel.nio.NioEventLoopGroup;

public class CustomInjectorSource extends AbstractModule implements InjectorSource{

	@Override
	public Injector getInjector() {
		 ScenarioModule scenarioModule = new ScenarioModule(new SequentialScenarioScope());
         return Guice.createInjector(scenarioModule,this);
	}

	@Override
	protected void configure() {
		KubeClient client = KubeClient.builder()
				.eventLoop(new NioEventLoopGroup())
				.server("http://localhost:8080")
			.verbose(true)
			.build();
		
		bind(KubeClient.class).toInstance(client);
	}
	
	@Provides
	@ScenarioScoped
	public Map<String,Object> scenarioData(){
		return Maps.newHashMap();
	}

}
