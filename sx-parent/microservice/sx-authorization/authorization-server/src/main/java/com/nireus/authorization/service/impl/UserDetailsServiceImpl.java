package com.nireus.authorization.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nireus.admin.client.UserFeignClient;
import com.nireus.admin.dto.UserDTO;
import com.nireus.authorization.domain.User;
import com.nireus.common.result.Result;
import com.nireus.common.result.ResultCode;
import com.nireus.common.util.RequestUtils;

/**
 * 
 * <p>
 * 自定义获取用户信息
 * </p>
 * 
 * @author wushixiong
 * @since 2021-05-26 18:18:03
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	@Autowired
	private UserFeignClient userFeignClient;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		String clientId = RequestUtils.getAuthClientId();// 区别前端还是管理后台用户
		User user = null;
		Result<?> result = null;
		result = userFeignClient.getUserByUsername(username);
		log.info("获取用户信息：{}", result.toString());
		if (ResultCode.SUCCESS.getCode().equals(result.getCode())) {
			UserDTO userDTO = (UserDTO) result.getData();
			user = new User(userDTO);
		}
		if (user == null || user.getId() == null) {
			throw new UsernameNotFoundException(ResultCode.USER_NOT_EXIST.getMsg());
		} else if (!user.isEnabled()) {
			throw new DisabledException("该账户已被禁用!");
		} else if (!user.isAccountNonLocked()) {
			throw new LockedException("该账号已被锁定!");
		} else if (!user.isAccountNonExpired()) {
			throw new AccountExpiredException("该账号已过期!");
		}
		return user;
	}

}
