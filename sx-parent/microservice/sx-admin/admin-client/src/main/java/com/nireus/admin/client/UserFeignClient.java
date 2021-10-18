package com.nireus.admin.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nireus.admin.client.fallback.UserFeignFallbackClient;
import com.nireus.admin.dto.UserDTO;
import com.nireus.common.result.Result;

/**
 * 
 * <p>
 * 微服务调用接口
 * </p>
 * 
 * @author wushixiong
 * @since 2021-05-28 18:03:11
 */
@FeignClient(value = "sx-admin", fallback = UserFeignFallbackClient.class)
public interface UserFeignClient {

	/**
	 * 
	 * <p>
	 * 	通过用户名获取用户信息
	 * </p>
	 * 
	 * @param username
	 * @return
	 * @create wushixiong 2021-05-28 18:04:44
	 */
	@GetMapping("/user/username/{username}")
	Result<UserDTO> getUserByUsername(@PathVariable String username);
}
