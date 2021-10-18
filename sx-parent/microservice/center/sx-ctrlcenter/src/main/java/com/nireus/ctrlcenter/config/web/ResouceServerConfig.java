package com.nireus.ctrlcenter.config.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * 
 * <p>
 * 资源服务配置类
 * </p>
 * 
 * @author wushixiong
 * @since 2021-05-16 13:16:53
 */
@Configuration
public class ResouceServerConfig extends ResourceServerConfigurerAdapter {

	private final String SIGN_KEY = "auth";
	public static final String RESOURCE_ID = "res1";

	/**
	 * 资源服务安全配置
	 */
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.resourceId(RESOURCE_ID).tokenStore(tokenStore()).stateless(true);
	}

	/**
	 * 
	 * <p>
	 * token存储方式
	 * </p>
	 * 
	 * @return
	 * @create wushixiong 2021-05-16 13:32:05
	 */
	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
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
		converter.setSigningKey(SIGN_KEY); // 对称秘钥，资源服务器使用该秘钥来验证
		return converter;
	}
}
