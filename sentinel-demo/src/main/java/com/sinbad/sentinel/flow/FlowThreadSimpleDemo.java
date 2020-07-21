package com.sinbad.sentinel.flow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.util.TimeUtil;

/**
 * @author jialiang.linjl
 * sinbad copy on 2020/07/11.
 */
public class FlowThreadSimpleDemo {

	public static void main(String[] args) {
		initFlowRule();

		int times = 2;
		while (times-- > 0) {

			/**
			 * 上线文的定义  name 构建调用链
			 * 					比如
			 * 						从Native端来的请求
			 * 						从H5来的请求
			 * 			  origin: 	用作用作权限的划分
			 *
			 * 	context   决定了链路节点node
			 * 	resource  决定了统计信息
			 *
			 *
			 *
			 */
			ContextUtil.enter("context-Name", "context-Origin");
			//ContextUtil.enter("context-Name");
			Entry methodA = null;
			try {
				methodA = SphU.entry("methodA");
			} catch (BlockException e1) {
				e1.printStackTrace();
			} catch (Exception e2) {
				// biz exception
			} finally {
				if (methodA != null) {
					methodA.exit();
				}
			}
			try {
				methodA = SphU.entry("methodA");
				AtomicInteger atomicInteger = new AtomicInteger();
				atomicInteger.addAndGet(1);
			} catch (BlockException e) {
				e.printStackTrace();
			}
		}
	}

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
	}


}
