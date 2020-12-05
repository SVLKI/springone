package com.cast.springhzel;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.discovery.EurekaClientConfig;

@Configuration
public class EurekaClientConfiguration {

	@Bean(name = "eurekaClientConfigBean")
	public EurekaClientConfig eurekaClientConfig(@Value("${clientid:admin}") String uiClientId,
			@Value("${secret:admin}") String uiClientSecret,
			@Value("${eureka.client.config.defaultUrl:localhost:9761/eureka/}") String eurekaClientUri)
			throws Exception {
		EurekaClientConfigBean eurekaClientConfig = new EurekaClientConfigBean();
		Map<String, String> urls = new HashMap<>();
		String defaultZone = "http://" + uiClientId + ":" + uiClientSecret + "@" + eurekaClientUri;
		urls.put("defaultZone", defaultZone);
		eurekaClientConfig.setServiceUrl(urls);
		return eurekaClientConfig;
	}
}