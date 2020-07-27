package com.sinbad.demo.webdemo.service;


import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.stereotype.Service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HystrixServiceImpl {


	//触发打开断路器的配置
	@HystrixCommand(
			groupKey = "hystrixDefault",

			commandProperties = {
					//监控 窗口时间
					@HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "3000"),
					//请求数
					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2"),
					//失败请求率
					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
					//重试时间窗口
//					@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds ", value = "3000"),
			}, fallbackMethod = "fallBack"
	)
	public String hystrixDefault() {
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			return e.getLocalizedMessage();
		}
		return "success";

	}


	//触发打开断路器的配置
	@HystrixCommand(
			groupKey = "hystrixTimeOut",
			commandProperties = {
					//监控 窗口时间
					@HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "3000"),
					//请求数
					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2"),
					//失败请求率
					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
					//重试时间窗口
//					@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds ", value = "3000"),
			}, fallbackMethod = "fallBack"
	)
	public String hystrixTimeOut() {
		log.info("Do try, uuid={} date={}", UUID.randomUUID(), new Date().toString());
		try {
			TimeUnit.MILLISECONDS.sleep(RandomUtils.nextInt(500) + 200);
		} catch (InterruptedException e) {
			return e.getLocalizedMessage();
		}
		return "success";

	}


	//触发打开断路器的配置
	@HystrixCommand(
			groupKey = "hystrixError",
			commandProperties = {
					//监控 窗口时间
					@HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "3000"),
					//请求数
					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2"),
					//失败请求率
					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
					//重试时间窗口
//					@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds ", value = "3000"),
			}, fallbackMethod = "fallBack"
	)
	public String hystrixError() {

		try {
			return String.valueOf(1 / RandomUtils.nextInt(2));
		} catch (Exception e) {
			throw e;
		}
	}


	public String fallBack() {
		return "fallBack";
	}
}
