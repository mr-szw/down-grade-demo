package com.sinbad.sentinel.flow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.util.TimeUtil;

/**
 * @author jialiang.linjl
 * sinbad copy on 2020/07/11.
 */
public class FlowThreadSimpleDemo {

	private static final int threadCount = 300;

	public static void main(String[] args) throws InterruptedException {
		initFlowRule();


		AnnotationTest flowThreadSimpleDemo = new AnnotationTest();

		for (int i = 0; i < threadCount; i++) {
			Thread entryThread = new Thread(() -> {
				while (true) {
					String testMethod = flowThreadSimpleDemo.testMethod(System.currentTimeMillis());
					System.out.println(testMethod);
				}
			});
			entryThread.setName("working-thread");
			entryThread.start();
		}

		TimeUnit.MILLISECONDS.sleep(10000);
	}
//public static void main(String[] args) {
//		initFlowRule();
//
//		int times = 200;
//		while (times-- > 0) {
//
//			/**
//			 * 上线文的定义  name 构建调用链
//			 * 					比如
//			 * 						从Native端来的请求
//			 * 						从H5来的请求
//			 * 			  origin: 	用作用作权限的划分
//			 *
//			 * 	context   决定了链路节点node
//			 * 	resource  决定了统计信息
//			 *
//			 *
//			 *
//			 */
//			ContextUtil.enter("context-Name", "context-Origin");
//			//ContextUtil.enter("context-Name");
//			Entry methodA = null;
//			try {
//				methodA = SphU.entry("methodA");
//			} catch (BlockException e1) {
//				e1.printStackTrace();
//			} catch (Exception e2) {
//				// biz exception
//			} finally {
//				if (methodA != null) {
//					methodA.exit();
//				}
//			}
//			try {
//				methodA = SphU.entry("methodA");
//				AtomicInteger atomicInteger = new AtomicInteger();
//				atomicInteger.addAndGet(1);
//			} catch (BlockException e) {
//				e.printStackTrace();
//			}
//		}
//	}

	private static void initFlowRule() {
		List<FlowRule> rules = new ArrayList<>();
		FlowRule rule1 = new FlowRule();
		rule1.setResource("methodA");
		// set limit concurrent thread for 'methodA' to 20
		rule1.setGrade(RuleConstant.FLOW_GRADE_THREAD);
		rule1.setCount(1);
		rule1.setLimitApp("default");

		rules.add(rule1);
		FlowRuleManager.loadRules(rules);


		List<DegradeRule> degradeList = new ArrayList<>();
		DegradeRule degradeRule = new DegradeRule();
		degradeRule.setResource("testMethod");
		// set limit concurrent thread for 'methodA' to 20
		degradeRule.setGrade(RuleConstant.DEGRADE_GRADE_RT);
		degradeRule.setCount(10);
		degradeRule.setTimeWindow(10);
		degradeRule.setLimitApp("default");

		degradeList.add(degradeRule);
		boolean validRule = DegradeRuleManager.isValidRule(degradeRule);
		if (validRule) {
			DegradeRuleManager.loadRules(degradeList);
		}
	}


	@SentinelResource(value = "testMethod", blockHandler = "blockHandler", fallback = "fallbackHandler")
	public String testMethod() {


		try {
			TimeUnit.MILLISECONDS.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Success");
		return "Success";
	}


	public String blockHandler(BlockException blockE) {
		System.out.println("blockHandler");
		blockE.printStackTrace();
		return "blockHandler";
	}

	public String fallbackHandler(BlockException blockE) {
		System.out.println("fallbackHandler");
		blockE.printStackTrace();
		return "fallbackHandler";
	}

}
