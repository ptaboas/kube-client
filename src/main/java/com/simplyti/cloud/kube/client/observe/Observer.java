package com.simplyti.cloud.kube.client.observe;

import com.simplyti.cloud.kube.client.domain.Event;
import com.simplyti.cloud.kube.client.domain.KubernetesResource;

@FunctionalInterface
public interface Observer<T extends KubernetesResource> {
	
	void newEvent(Event<T> event);

}
