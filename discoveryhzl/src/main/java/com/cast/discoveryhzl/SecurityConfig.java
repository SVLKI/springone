package com.cast.discoveryhzl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.netflix.discovery.EurekaClientConfig;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${registry.security.enable-resource-server:true}")
	boolean enableResourceServerSecurity;

	/*@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth, @Value("${clientId:admin}") String uiClientId,
			@Value("${secret:admin}") String uiClientSecret) throws Exception {

		auth.inMemoryAuthentication().withUser(uiClientId).password({noop}uiClientSecret).roles("SERVICE");
	}*/

	@Override
    protected void configure(AuthenticationManagerBuilder auth) 
      throws Exception {
        auth.inMemoryAuthentication()
          .withUser("admin")
          .password("{noop}admin")
          .roles("USER");
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.authorizeRequests().anyRequest().authenticated().and().httpBasic().and().csrf().disable();

	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		if (!enableResourceServerSecurity) {
			web.ignoring().antMatchers("/**");
		}
	}

	@Bean
	public EurekaClientConfig eurekaClientConfig(@Value("${username:admin}") String uiClientId,
			@Value("${password:admin}") String uiClientSecret,
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