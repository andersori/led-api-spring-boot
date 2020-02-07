package io.andersori.led.api.app.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.assistant.v2.Assistant;

@Configuration
public class IbmConfig {

	@Value("${ibm.api.key}")
	private String key;
	@Value("${ibm.api.url}")
	private String url;

	@Bean
	public Assistant assistant() {
		Assistant assistant = new Assistant("2019-02-28", new IamAuthenticator(key));
		assistant.setServiceUrl(url);
		return assistant;
	}
	
}
