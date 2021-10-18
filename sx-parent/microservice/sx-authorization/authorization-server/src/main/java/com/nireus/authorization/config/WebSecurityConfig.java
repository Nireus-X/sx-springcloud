package com.nireus.authorization.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 
 * <p>
 * 应用访问安全配置
 * </p>
 * 
 * @author wushixiong
 * @since 2021-05-14 10:09:17
 */
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;

	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
////		auth.inMemoryAuthentication().withUser("admin").password(bCryptPasswordEncoder.encode("123")).roles("admin");
//		auth.jdbcAuthentication().passwordEncoder(bCryptPasswordEncoder);
//	}

	// 安全拦截机制（最重要）
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests().antMatchers("/login*").permitAll().and().authorizeRequests()
				.antMatchers("/getPublicKey").permitAll().anyRequest().authenticated().and().formLogin();
	}
}
