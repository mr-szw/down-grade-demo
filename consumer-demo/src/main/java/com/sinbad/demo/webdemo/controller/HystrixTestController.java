package com.sinbad.demo.webdemo.controller;


import javax.annotation.Resource;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sinbad.demo.utils.GsonUtil;
import com.sinbad.demo.webdemo.service.HystrixServiceImpl;
import com.sinbad.demo.webdemo.service.SentinelServiceImpl;

@RestController
@RequestMapping(value = "/hystrix")
public class HystrixTestController {


	@Resource
	private HystrixServiceImpl hystrixService;


	@GetMapping(value = "/get/default")
	public String hystrixDefault() {

		return hystrixService.hystrixDefault();
	}

	@GetMapping(value = "/get/timeout")
	public String sentinelTimeOut() {

		return hystrixService.hystrixTimeOut();
	}

	@GetMapping(value = "/get/error")
	public String sentinelError() {

		return hystrixService.hystrixError();
	}






}
