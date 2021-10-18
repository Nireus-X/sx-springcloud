package com.nireus.authorization.config;

import java.security.KeyPair;
import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.cloud.bootstrap.encrypt.KeyProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

/**
 * 
 * <p>
 * 授权服务配置bean
 * </p>
 * 
 * @author wushixiong
 * @since 2021-05-14 09:59:04
 */
@Configuration
public class AuthorizationConfig {

	/**
	 * 
	 * <p>
	 * token存储方式
	 * </p>
	 * 
	 * @return
	 * @create wushixiong 2021-05-14 09:59:40
	 */
	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	/**
	 * 
	 * <p>
	 * 私钥配置信息
	 * </p>
	 * 
	 * @return
	 * @create wushixiong 2021-05-16 15:23:41
	 */
	@Bean
	public KeyProperties keyProperties() {
		return new KeyProperties();
	}

	/**
	 * 从classpath下的密钥库中获取密钥对(公钥+私钥)
	 */
	@Bean
	public KeyPair keyPair() {
		KeyProperties keyProperties = this.keyProperties();
		KeyPair keyPair = new KeyStoreKeyFactory(keyProperties.getKeyStore().getLocation(),
				keyProperties.getKeyStore().getSecret().toCharArray()).getKeyPair(
						keyProperties.getKeyStore().getAlias(),
						keyProperties.getKeyStore().getPassword().toCharArray());
		return keyPair;
	}

	/**
	 * 
	 * <p>
	 * jwt token转换器配置
	 * </p>
	 * 
	 * @return
	 * @create wushixiong 2021-05-16 13:24:19
	 */
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		// 非对称加密
		converter.setKeyPair(this.keyPair());
		return converter;
	}

	/**
	 * 
	 * <p>
	 * 客户端配置详情从数据库读取
	 * </p>
	 * 
	 * @param dataSource
	 * @return
	 * @create wushixiong 2021-05-16 13:44:51
	 */
	@Bean("customClientDetailsService")
	public ClientDetailsService customClientDetailsService(DataSource dataSource) {
		ClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
		((JdbcClientDetailsService) clientDetailsService).setPasswordEncoder(bCryptPasswordEncoder());
		return clientDetailsService;
	}

	/**
	 * 
	 * <p>
	 * 授权服务端token服务配置
	 * </p>
	 * 
	 * @return token服务
	 * @create wushixiong 2021-05-14 09:49:49
	 */
	@Bean
	public AuthorizationServerTokenServices authorizationServerTokenServices(DataSource dataSource) {
		DefaultTokenServices service = new DefaultTokenServices();
		service.setClientDetailsService(this.customClientDetailsService(dataSource));
		service.setSupportRefreshToken(true);
		service.setTokenStore(tokenStore());
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter()));
		service.setTokenEnhancer(tokenEnhancerChain);
		service.setAccessTokenValiditySeconds(7200); // 令牌默认有效期2小时
		service.setRefreshTokenValiditySeconds(259200); // 刷新令牌默认有效期3天
		return service;
	}

	/**
	 * 
	 * <p>
	 * 授权码存储方式
	 * </p>
	 * 
	 * @return
	 * @create wushixiong 2021-05-14 09:58:13
	 */
	@Bean
	public AuthorizationCodeServices authorizationCodeServices(DataSource dataSource) {
		return new JdbcAuthorizationCodeServices(dataSource);
	}

	/**
	 * 
	 * <p>
	 * 密码加密
	 * </p>
	 * 
	 * @return
	 * @create wushixiong 2021-05-14 10:09:53
	 */
	@Bean
	public PasswordEncoder bCryptPasswordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}
