package com.nireus.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.WebFilter;

import com.nireus.common.constant.AuthConstants;
import com.nireus.common.result.ResultCode;
import com.nireus.gateway.util.WebUtils;

import cn.hutool.core.util.ArrayUtil;
import reactor.core.publisher.Mono;

/**
 * 
 * <p>
 * 权限认证配置类
 * </p>
 * 
 * @author wushixiong
 * @since 2021-05-16 16:46:38
 */
@Configuration
@EnableWebFluxSecurity
public class ResourceServerConfig {

	private static final String MAX_AGE = "18000L";

	@Autowired
	private AuthorizationManager authorizationManager;
	@Autowired
	private WhiteListConfig whiteListConfig;

	/**
	 * 跨域配置
	 */
	public WebFilter corsFilter() {
		return (ctx, chain) -> {
			ServerHttpRequest request = ctx.getRequest();
			if (CorsUtils.isCorsRequest(request)) {
				HttpHeaders requestHeaders = request.getHeaders();
				ServerHttpResponse response = ctx.getResponse();
				HttpMethod requestMethod = requestHeaders.getAccessControlRequestMethod();
				HttpHeaders headers = response.getHeaders();
				headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, requestHeaders.getOrigin());
				headers.addAll(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
						requestHeaders.getAccessControlRequestHeaders());
				if (requestMethod != null) {
					headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, requestMethod.name());
				}
				headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
				headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "*");
				headers.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, MAX_AGE);
				if (request.getMethod() == HttpMethod.OPTIONS) {
					response.setStatusCode(HttpStatus.OK);
					return Mono.empty();
				}
			}
			return chain.filter(ctx);
		};
	}

	/**
	 * 
	 * <p>
	 * token 转换配置
	 * </p>
	 * 
	 * @return
	 * @create wushixiong 2021-05-16 16:45:01
	 */
	@Bean
	public Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAccessTokenConverter() {
		JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		jwtGrantedAuthoritiesConverter.setAuthorityPrefix(AuthConstants.AUTHORITY_PREFIX);
		jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(AuthConstants.JWT_AUTHORITIES_KEY);

		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
		return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
	}

	/**
	 * 
	 * <p>
	 * 请求权限认证过滤器
	 * </p>
	 * 
	 * @param http
	 * @return
	 * @create wushixiong 2021-05-16 16:45:45
	 */
	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		http.oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAccessTokenConverter());
		// 无效token
		http.oauth2ResourceServer().authenticationEntryPoint(authenticationEntryPoint());

		http.authorizeExchange().pathMatchers(ArrayUtil.toArray(whiteListConfig.getUrls(), String.class)).permitAll()
				.anyExchange().access(authorizationManager).and().exceptionHandling()
				.accessDeniedHandler(accessDeniedHandler()) // 处理未授权
				.authenticationEntryPoint(authenticationEntryPoint()) // 处理未认证
				.and().csrf().disable();
		return http.build();
	}

	/**
	 * 处理token未认证
	 */
	@Bean
	ServerAuthenticationEntryPoint authenticationEntryPoint() {
		return (exchange, e) -> {
			Mono<Void> mono = Mono.defer(() -> Mono.just(exchange.getResponse()))
					.flatMap(response -> WebUtils.writeFailedToResponse(response, ResultCode.TOKEN_INVALID_OR_EXPIRED));
			return mono;
		};
	}

	/**
	 * 未授权
	 *
	 * @return
	 */
	@Bean
	ServerAccessDeniedHandler accessDeniedHandler() {
		return (exchange, denied) -> {
			Mono<Void> mono = Mono.defer(() -> Mono.just(exchange.getResponse()))
					.flatMap(response -> WebUtils.writeFailedToResponse(response, ResultCode.ACCESS_UNAUTHORIZED));
			return mono;
		};
	}

}
