package com.nireus.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 
 * <p>
 * 网关微服务启动类
 * </p>
 * 
 * @author wushixiong
 * @since 2021-05-16 14:47:36
 */
@EnableDiscoveryClient
@SpringBootApplication
public class SxGatewayApplication {
	public static void main(String[] args) {
		SpringApplication.run(SxGatewayApplication.class, args);
	}
}
