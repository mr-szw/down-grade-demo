package com.sinbad.demo.webdemo;


import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


/**
 * 支持spring boot功能
 * 支持 注册服务
 * 支持 断路器
 */
@SpringCloudApplication
//@SpringBootApplication
//@EnableCircuitBreaker
public class ConsumerDemoApplication {


	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}


	public static void main(String[] args) {
		SpringApplication.run(ConsumerDemoApplication.class, args);
	}
}
