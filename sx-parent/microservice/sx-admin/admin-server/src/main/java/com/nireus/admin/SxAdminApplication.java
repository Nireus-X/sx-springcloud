package com.nireus.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@MapperScan(basePackages = { "com.nireus.admin.mapper" })
@EnableEurekaClient
@SpringBootApplication
public class SxAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(SxAdminApplication.class, args);
	}
}
