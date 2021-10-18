package com.nireus.admin.client.fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.nireus.admin.client.UserFeignClient;
import com.nireus.admin.dto.UserDTO;
import com.nireus.common.result.Result;
import com.nireus.common.result.ResultCode;

/**
 * 
 * <p>降级服务</p>
 * @author wushixiong
 * @since 2021-05-28 17:58:59
 */
@Component
public class UserFeignFallbackClient implements UserFeignClient {
	
	private static final Logger log = LoggerFactory.getLogger(UserFeignFallbackClient.class);

	@Override
	public Result<UserDTO> getUserByUsername(String username) {
		log.error("feign远程调用系统用户服务异常后的降级方法");
		return Result.failed(ResultCode.DEGRADATION);
	}
}
