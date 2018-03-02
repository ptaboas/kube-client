package com.simplyti.cloud.kube.client.reqs;

import com.google.common.base.Splitter;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringEncoder;
import lombok.Getter;

@Getter
public class KubernetesCommandExec extends KubernetesApiRequest {

	public KubernetesCommandExec(String namespace, String name, String container, String command) {
		super(HttpMethod.POST, uri(namespace, name, container, command), null);
	}

	private static String uri(String namespace, String name, String container, String command) {
		QueryStringEncoder encoder = new QueryStringEncoder("/api/v1/namespaces/"+namespace+"/pods/"+name+"/exec");
		Splitter.on(' ').split(command).forEach(part->encoder.addParam("command", part));
		encoder.addParam("container", container);
		encoder.addParam("stdout", "true");
		encoder.addParam("stderr", "true");
		return encoder.toString();
	}

}
