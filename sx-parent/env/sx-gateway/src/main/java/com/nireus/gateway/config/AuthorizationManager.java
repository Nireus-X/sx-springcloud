package com.nireus.gateway.config;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.nireus.common.constant.AuthConstants;

import cn.hutool.core.convert.Convert;
import reactor.core.publisher.Mono;

/**
 * 
 * <p>
 * 自定义授权管理
 * </p>
 * 
 * @author wushixiong
 * @since 2021-05-16 16:13:23
 */
@Component
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

	private static final Logger log = LoggerFactory.getLogger(AuthorizationManager.class);

	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 * 检查权限
	 */
	@Override
	public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
		ServerHttpRequest request = authorizationContext.getExchange().getRequest();
		// Restful接口权限设计
		String restPath = request.getMethodValue() + "_" + request.getURI().getPath();
		log.info("请求路径：{}", restPath);
		PathMatcher pathMatcher = new AntPathMatcher();
		// 对应跨域的预检请求直接放行
		if (request.getMethod() == HttpMethod.OPTIONS) {
			return Mono.just(new AuthorizationDecision(true));
		}
		// 非管理端路径无需鉴权直接放行
		if (!pathMatcher.match(AuthConstants.ADMIN_URL_PATTERN, restPath)) {
			log.info("请求无需鉴权，请求路径：{}", restPath);
			return Mono.just(new AuthorizationDecision(true));
		}
		// 从缓存取资源权限角色关系列表
		Map<Object, Object> permissionRoles = redisTemplate.opsForHash().entries(AuthConstants.PERMISSION_ROLES_KEY);
		Iterator<Object> iterator = permissionRoles.keySet().iterator();
		// 请求路径匹配到的资源需要的角色权限集合authorities统计
		Set<String> authorities = new HashSet<>();
		while (iterator.hasNext()) {
			String pattern = (String) iterator.next();
			if (pathMatcher.match(pattern, restPath)) {
				authorities.addAll(Convert.toList(String.class, permissionRoles.get(pattern)));
			}
		}

		Mono<AuthorizationDecision> authorizationDecisionMono = mono.filter(Authentication::isAuthenticated)
				.flatMapIterable(Authentication::getAuthorities).map(GrantedAuthority::getAuthority).any(roleId -> {
					// roleId是请求用户的角色(格式:ROLE_{roleId})，authorities是请求资源所需要角色的集合
					log.info("访问路径：{}", restPath);
					log.info("用户角色：{}", roleId);
					log.info("资源需要角色：{}", authorities);
					return authorities.contains(roleId);
				}).map(AuthorizationDecision::new).defaultIfEmpty(new AuthorizationDecision(false));

		return authorizationDecisionMono;
	}
}
