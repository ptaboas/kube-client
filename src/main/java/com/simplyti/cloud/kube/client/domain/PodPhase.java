package com.simplyti.cloud.kube.client.domain;

import com.fasterxml.jackson.annotation.JsonCreator;


public enum PodPhase {
	
	PENDING,RUNNING,SUCCEEDED,FAILED,UNKNOWN;
	
	
	@JsonCreator
    public static PodPhase forValue(String value) {
        return PodPhase.valueOf(value.toUpperCase());
    }

}
