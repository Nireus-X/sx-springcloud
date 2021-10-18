package com.nireus.redis.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * <p>
 * Redisson 连接配置
 * </p>
 * 
 * @author wushixiong
 * @since 2021-05-26 17:39:40
 */
@Configuration
public class RedissonConfig {

	@Autowired
	private RedissonProperties redissonProperties;

	@Bean
	public RedissonClient redissonClient() {
		if (redissonProperties.getServerAddress() == null) {
			return null;
		}
		Config config = new Config();
		SingleServerConfig singleServerConfig = config.useSingleServer();
		singleServerConfig
				// 可以用"rediss://"来启用SSL连接
				.setAddress(redissonProperties.getServerAddress() + ":" + redissonProperties.getPort())
				.setPassword(redissonProperties.getPassword());
		RedissonClient redisson = Redisson.create(config);
		return redisson;
	}

}
