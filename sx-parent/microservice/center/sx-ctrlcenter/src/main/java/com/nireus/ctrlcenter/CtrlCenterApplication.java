package com.nireus.ctrlcenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class CtrlCenterApplication {
	public static void main(String[] args) {
		SpringApplication.run(CtrlCenterApplication.class, args);
	}
}
