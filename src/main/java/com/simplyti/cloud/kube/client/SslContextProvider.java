package com.simplyti.cloud.kube.client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.function.Supplier;

import javax.net.ssl.SSLException;

import com.google.common.base.MoreObjects;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SslContextProvider implements Supplier<SslContext>{

	private static final String DEFAULT_CA_FILE = "/var/run/secrets/kubernetes.io/serviceaccount/ca.crt";
	private static final String CERT_TYPE = "X.509";
	
	private SslContext sslCtx=null;

	public SslContextProvider(boolean secured, String caFile) {
		if(!secured){
			return;
		}
		Path path = Paths.get(MoreObjects.firstNonNull(caFile, DEFAULT_CA_FILE));
		if(Files.exists(path)){
			InputStream inputStream = null;
			try {
				inputStream = new FileInputStream(path.toFile());
				CertificateFactory certFactory = CertificateFactory.getInstance(CERT_TYPE);
				X509Certificate certificate = (X509Certificate) certFactory.generateCertificate(inputStream);
				this.sslCtx = SslContextBuilder.forClient()
						.trustManager(certificate).build();
			} catch (CertificateException | FileNotFoundException | SSLException e) {
				log.warn("Cannot create ssl context",e);
			}finally{
				try {inputStream.close();} catch (IOException e) {}
			}
		}
	}

	@Override
	public SslContext get() {
		return sslCtx;
	}

}
