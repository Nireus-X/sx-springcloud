package com.nireus.authorization.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.nireus.authorization.domain.User;
import com.nireus.authorization.service.impl.JdbcClientDetailsServiceImpl;
import com.nireus.authorization.service.impl.UserDetailsServiceImpl;
import com.nireus.common.constant.AuthConstants;
import com.nireus.common.result.Result;
import com.nireus.common.result.ResultCode;
import com.nireus.common.util.JsonUtils;

/**
 * 
 * <p>
 * 授权服务配置
 * </p>
 * 
 * @author wushixiong
 * @since 2021-05-14
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private JwtAccessTokenConverter jwtAccessTokenConverter;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private DataSource dataSource;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	/**
	 * 客户端授权配置
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsServiceImpl(dataSource);
		jdbcClientDetailsService.setFindClientDetailsSql(AuthConstants.FIND_CLIENT_DETAILS_SQL);
		jdbcClientDetailsService.setSelectClientDetailsSql(AuthConstants.SELECT_CLIENT_DETAILS_SQL);
		jdbcClientDetailsService.setPasswordEncoder(passwordEncoder);
		clients.withClientDetails(jdbcClientDetailsService);
	}

	/**
	 * 配置令牌访问端点
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//		endpoints.authenticationManager(authenticationManager)
//				.authorizationCodeServices(authorizationCodeServices)
//				.tokenServices(authorizationServerTokenServices).allowedTokenEndpointRequestMethods(HttpMethod.POST);
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
		tokenEnhancers.add(tokenEnhancer());
		tokenEnhancers.add(jwtAccessTokenConverter);
		tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);
		endpoints.authenticationManager(authenticationManager).accessTokenConverter(jwtAccessTokenConverter)
				.tokenEnhancer(tokenEnhancerChain).userDetailsService(userDetailsService).reuseRefreshTokens(true);
	}

	/**
	 * 端点访问安全配置 permitAll() 完全公开
	 */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()").checkTokenAccess("permitAll()").allowFormAuthenticationForClients();
	}

	/**
	 * JWT内容增强
	 */
	@Bean
	public TokenEnhancer tokenEnhancer() {
		return (accessToken, authentication) -> {
			Map<String, Object> map = new HashMap<>(8);
			User user = (User) authentication.getUserAuthentication().getPrincipal();
			map.put(AuthConstants.USER_ID_KEY, user.getId());
			map.put(AuthConstants.CLIENT_ID_KEY, user.getClientId());
			map.put(AuthConstants.USER_NAME_KEY, user.getUsername());
			((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(map);
			return accessToken;
		};
	}

	/**
	 * 自定义认证异常响应数据
	 */
	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return (request, response, e) -> {
			response.setStatus(HttpStatus.OK.value());
			response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Cache-Control", "no-cache");
			Result result = Result.failed(ResultCode.CLIENT_AUTHENTICATION_FAILED);
			response.getWriter().print(JsonUtils.toJson(result));
			response.getWriter().flush();
		};
	}

	/**
	 * 
	 * <p>
	 * 	验证登录用户密码是否正确
	 * </p>
	 * 
	 * @return
	 * @create wushixiong 2021-05-31 14:16:15
	 */
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setHideUserNotFoundExceptions(false); // 用户不存在异常抛出
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
	}

}
