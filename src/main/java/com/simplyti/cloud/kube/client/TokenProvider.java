package com.simplyti.cloud.kube.client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;

import com.google.common.base.MoreObjects;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class TokenProvider implements Supplier<String>{

	private static final String DEFAULT_TOKEN_FILE = "/var/run/secrets/kubernetes.io/serviceaccount/token";
	private String token;

	public TokenProvider(String tokenFile) {
		Path path = Paths.get(MoreObjects.firstNonNull(tokenFile, DEFAULT_TOKEN_FILE));
		if(Files.exists(path)){
			try {
				this.token = readFile(path);
			} catch (IOException e) {
				log.warn("Cannot read token",e);
			}
		}
	}

	private String readFile(Path path) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(path.toFile()));
		try {
		    return  br.readLine();
		} finally {
		    br.close();
		}
	}

	@Override
	public String get() {
		return token;
	}

}
