package com.nireus.authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.nireus.admin.client.UserFeignClient;

/**
 * 
 * <p>
 * 授权微服务启动类
 * </p>
 * 
 * @author wushixiong
 * @since 2021-05-14
 */
@EnableFeignClients(basePackageClasses = {UserFeignClient.class})
@EnableDiscoveryClient
@SpringBootApplication
public class AuthorizationApplication {
	public static void main(String[] args) {
		SpringApplication.run(AuthorizationApplication.class, args);
	}
}
