package com.cast.springhzel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.context.annotation.Bean;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.XmlClientConfigBuilder;
import com.hazelcast.config.ClasspathXmlConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.DiscoveryStrategyConfig;
import com.hazelcast.config.FileSystemXmlConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.eureka.one.EurekaOneDiscoveryStrategyFactory;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaClientConfig;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringhzelApplication {

	@Value("${hazelcast.port:6701}")
	private int hazelcastPort;

	@Value("${hazelcast.config.file.path:C:/Devbox/hazelcast.xml}")
	private String hazelcastConfigFilePath;

	@Value("${hazelcast_client.config.file.path:C:/Devbox/hazelcast-client.xml}")
	private String hazelcastClientConfigFilePath;

	public static void main(String[] args) {
		SpringApplication.run(SpringhzelApplication.class, args);
	}

	/*
	 * @Bean public Config hazelcastConfig() { Config config = new Config();
	 * config.getNetworkConfig().setPort(hazelcastPort);
	 * config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false
	 * );
	 * config.getNetworkConfig().getJoin().getEurekaConfig().setEnabled(true).
	 * setProperty("self-registration", "true") .setProperty("namespace",
	 * "hazelcast"); return config; }
	 */

	@Bean(name = "hazelcastInstance")
	public HazelcastInstance getHazelcastInstance(EurekaClient eurekaClient) throws FileNotFoundException {

		HazelcastInstance hazelcastInstance = null;
		EurekaOneDiscoveryStrategyFactory.setEurekaClient(eurekaClient);
		Config hazelcastConfig = new FileSystemXmlConfig(hazelcastConfigFilePath);

		
 /*hazelcastConfig.getNetworkConfig().getJoin().getEurekaConfig().
		  setEnabled(true) .setProperty("self-registration", "true")
		  .setProperty("namespace", "hazelcast")
		  .setProperty("use-classpath-eureka-client-props","false")
		  .setProperty("shouldUseDns","false") .setProperty("name",
		  "hazelcast-separate-client") .setProperty("serviceUrl.default",
		  "http://admin:admin@localhost:9761/eureka/");*/
		 

		
		/*  DiscoveryStrategyConfig configFactory = new DiscoveryStrategyConfig(
		  "com.hazelcast.eureka.one.EurekaOneDiscoveryStrategy");*/
		 
		List<DiscoveryStrategyConfig> discoveryStrategyConfigs = ((List<DiscoveryStrategyConfig>) hazelcastConfig
				.getNetworkConfig().getJoin().getDiscoveryConfig().getDiscoveryStrategyConfigs());
		discoveryStrategyConfigs.get(0).addProperty("self-registration", "true");
		discoveryStrategyConfigs.get(0).addProperty("namespace", "hazelcast");
		discoveryStrategyConfigs.get(0).addProperty("use-classpath-eureka-client-props", "false");
		discoveryStrategyConfigs.get(0).addProperty("shouldUseDns", "false");
		discoveryStrategyConfigs.get(0).addProperty("name", "hazelcast-separate-client");
		discoveryStrategyConfigs.get(0).addProperty("serviceUrl.default", "http://admin:admin@localhost:9761/eureka/");
		// discoveryStrategyConfigs.get(0).addProperty(key, value);

		hazelcastInstance = Hazelcast.newHazelcastInstance(hazelcastConfig);

		return hazelcastInstance;
	}

	/*
	 * @Bean
	 * 
	 * @Qualifier("hazelcastClientInstance") public HazelcastInstance
	 * hazelcastClientInstance(EurekaClient eurekaClient) throws Exception { //
	 * This will create HAZELCAST CLIENT instance String serviceUrl=null;
	 * 
	 * HazelcastInstance hazelcastClientInstance = null;
	 * EurekaOneDiscoveryStrategyFactory.setEurekaClient(eurekaClient);
	 * 
	 * ClientConfig clientConfig = new
	 * XmlClientConfigBuilder(hazelcastClientConfigFilePath).build();
	 * 
	 * //Eureka changes List<DiscoveryStrategyConfig> discoveryStrategyConfigs =
	 * ((List<DiscoveryStrategyConfig>) clientConfig
	 * .getNetworkConfig().getDiscoveryConfig().getDiscoveryStrategyConfigs());
	 * discoveryStrategyConfigs.get(0).addProperty("self-registration",
	 * "false"); discoveryStrategyConfigs.get(0).addProperty("namespace",
	 * "hazelcast-client"); discoveryStrategyConfigs.get(0).addProperty(
	 * "use-classpath-eureka-client-props", "false");
	 * discoveryStrategyConfigs.get(0).addProperty("shouldUseDns", "false");
	 * discoveryStrategyConfigs.get(0).addProperty("name",
	 * "hazelcast-cacheservice");
	 * discoveryStrategyConfigs.get(0).addProperty("serviceUrl.default",
	 * "http://admin:admin@localhost:9761/eureka/"); //Eureka Changes
	 * hazelcastClientInstance =
	 * HazelcastClient.newHazelcastClient(clientConfig);
	 * 
	 * 
	 * return hazelcastClientInstance; }
	 */

	/*
	 * public EurekaClientConfig getBean() { EurekaClientConfigBean
	 * eurekaClientConfigBean = new EurekaClientConfigBean(); Map<String,
	 * String> urls = new HashMap<>() String defaultZone = "http://" + "admin" +
	 * ":" + "admin" + "@" + "localhost:9761/eureka/"; urls.put("defaultZone",
	 * defaultZone); eurekaClientConfigBean.setServiceUrl(urls); return
	 * eurekaClientConfigBean; }
	 */

	/*@Bean(name = "eurekaClientConfigBean")
	public EurekaClientConfig eurekaClientConfig() throws Exception {
		EurekaClientConfigBean eurekaClientConfigBean = new EurekaClientConfigBean();
		Map<String, String> urls = new HashMap<>();
		String defaultZone = "http://" + "admin" + ":"+"admin" + "@" + "localhost:9761/eureka/";
		urls.put("defaultZone", defaultZone);
		eurekaClientConfigBean.setServiceUrl(urls);
		return eurekaClientConfigBean;
	}*/
}
