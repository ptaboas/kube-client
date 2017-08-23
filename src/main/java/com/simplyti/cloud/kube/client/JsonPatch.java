package com.simplyti.cloud.kube.client;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JsonPatch {
	
	public enum PatchOperation{
		REPLACE, ADD;
	}
	
	private final PatchOperation op;
	private final String path;
	private final Object value;
	
	public static JsonPatch replace(String path, Object value) {
		return new JsonPatch(PatchOperation.REPLACE,path,value);
	}
	
	public static JsonPatch add(String path, Object value) {
		return new JsonPatch(PatchOperation.ADD,path,value);
	}

}
