package com.simplyti.cloud.kube.client;

import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLException;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.Getter;

@Getter
public class SecurityOptions {
	
	public static final SecurityOptions NONE = new SecurityOptions(false,null,null);

	private static final String CERT_TYPE = "X.509";
	
	private final boolean ssl;
	private final SslContext sslCtx;
	private final String token;
	
	public SecurityOptions(boolean ssl, InputStream caCertificate, String token){
		this.ssl=ssl;
		if(ssl==true){
			try {
				CertificateFactory certFactory = CertificateFactory.getInstance(CERT_TYPE);
				X509Certificate certificate = (X509Certificate) certFactory.generateCertificate(caCertificate);
				this.sslCtx = SslContextBuilder.forClient()
						.trustManager(certificate).build();
			} catch (SSLException | CertificateException e) {
				throw new RuntimeException(e);
			}
		}else{
			this.sslCtx=null;
		}
		this.token=token;
	}


}
