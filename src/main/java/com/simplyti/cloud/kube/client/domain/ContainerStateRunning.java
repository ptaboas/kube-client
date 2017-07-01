package com.simplyti.cloud.kube.client.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContainerStateRunning {
	
	private final LocalDateTime startedAt;

}
