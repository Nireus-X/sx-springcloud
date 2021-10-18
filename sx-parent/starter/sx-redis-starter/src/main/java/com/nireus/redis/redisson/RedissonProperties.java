package com.nireus.redis.redisson;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * <p>
 * redisson 连接配置类
 * </p>
 * 
 * @author wushixiong
 * @since 2021-05-26 17:37:50
 */
@Configuration
@ConfigurationProperties(prefix = "redisson")
public class RedissonProperties implements Serializable {

	private static final long serialVersionUID = 1L;

	private String serverAddress;

	private String port;

	private String password;

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
