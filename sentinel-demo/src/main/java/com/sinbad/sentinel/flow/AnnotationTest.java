package com.sinbad.sentinel.flow;

import java.util.concurrent.TimeUnit;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;

/**
 * @author sinbad on 2020/07/27.
 */
public class AnnotationTest {


	@SentinelResource(value = "testMethod", blockHandler = "blockHandler", fallback = "fallbackHandler")
	public String testMethod(long time) {


		try {

			TimeUnit.MILLISECONDS.sleep(100);
			System.out.println("IN Success");
			TimeUnit.MILLISECONDS.sleep(200);
			System.out.println("OUt Success");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "Success" + time;
	}


	public String blockHandler(long time, BlockException blockE) {
		System.out.println("blockHandler");
		blockE.printStackTrace();
		return "blockHandler" + time;
	}

	public String fallbackHandler(long time, BlockException blockE) {
		System.out.println("fallbackHandler");
		blockE.printStackTrace();
		return "fallbackHandler" + time;
	}

}
