package com.nireus.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 
 * <p>
 * 	白名单配置
 * </p>
 * 
 * @author wushixiong
 * @since 2021-05-26 17:15:27
 */
@Configuration
@ConfigurationProperties(prefix = "whitelist")
public class WhiteListConfig {

	private List<String> urls;

	public List<String> getUrls() {
		return urls;
	}

	public void setUrls(List<String> urls) {
		this.urls = urls;
	}

}
