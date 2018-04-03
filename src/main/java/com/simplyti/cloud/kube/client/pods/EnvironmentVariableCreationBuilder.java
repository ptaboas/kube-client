package com.simplyti.cloud.kube.client.pods;

import com.simplyti.cloud.kube.client.domain.EnvironmentVariableValueFrom;
import com.simplyti.cloud.kube.client.domain.EnvironmentVariableValueFromSpec;
import com.simplyti.cloud.kube.client.domain.FieldRef;

public class EnvironmentVariableCreationBuilder {

	private final String name;
	private final PodContainerCreationBuilder builder;

	public EnvironmentVariableCreationBuilder(String name, PodContainerCreationBuilder builder) {
		this.name = name;
		this.builder = builder;
	}

	public PodContainerCreationBuilder fromField(String field) {
		return builder.addEnvironmentVariable(new EnvironmentVariableValueFrom(name, new EnvironmentVariableValueFromSpec(new FieldRef(field))));
	}

}
