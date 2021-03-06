package com.jpmorgn.bank.verification.config;

import java.util.Map;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

/**
 * Class for MDC task decorator 
 * @author pravin
 *
 */
public class MdcTaskDecorator implements TaskDecorator {

	@Override
	public Runnable decorate(Runnable runnable) {
		Map<String, String> contextMap = MDC.getCopyOfContextMap();
		return()-> {
			try {
				MDC.setContextMap(contextMap);
				runnable.run();
			} finally {
				MDC.clear();
			}
		};
	}
}
