package com.nireus.eureka.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 
 * <p>
 * eureka控制台安全保护
 * </p>
 * 
 * @author wushixiong
 * @since 2021-05-12
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.csrf().ignoringAntMatchers("/eureka/**");
		super.configure(http);
	}
}
