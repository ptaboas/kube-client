package com.simplyti.cloud.kube.client.domain;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExecAction {
	
	private final Collection<String> command;

}
