package com.sinbad.demo.webdemo.service;


import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.stereotype.Service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SentinelServiceImpl {


	@SentinelResource(value = "SentinelServiceImpl.default")
	public String sentinelDefault() {
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			return e.getLocalizedMessage();
		}
		return "success";

	}


	@SentinelResource(value = "SentinelServiceImpl.sentinelTimeOut", fallback = "sentinelTimeOutFallBack", blockHandler = "sentinelErrorBlockHandler")
	public String sentinelTimeOut() {
		log.info("Do try, uuid={} date={}", UUID.randomUUID(), new Date().toString());
		try {
			TimeUnit.MILLISECONDS.sleep(RandomUtils.nextInt(500) + 200);
		} catch (InterruptedException e) {
			return e.getLocalizedMessage();
		}
		return "success";

	}

	public String sentinelTimeOutFallBack(Throwable throwable) {
		throwable.printStackTrace();
		return "Time out";
	}

	public String sentinelErrorBlockHandler(BlockException ex) {
		System.out.println("Error Block  out");
		ex.printStackTrace();
		return "Error Block  out";
	}


	@SentinelResource(value = "SentinelServiceImpl.sentinelError", fallback = "sentinelTimeOutFallBack", blockHandler = "sentinelErrorBlockHandler")
	public String sentinelError() {
		try {
			return String.valueOf(1 / RandomUtils.nextInt(2));
		} catch (Exception e) {
			throw e;
		}
	}

}
