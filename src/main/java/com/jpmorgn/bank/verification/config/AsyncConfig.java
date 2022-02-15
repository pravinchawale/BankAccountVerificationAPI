package com.jpmorgn.bank.verification.config;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.jpmorgn.bank.verification.exception.GlobalExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class AsyncConfig implements AsyncConfigurer {

	@Value("${async.conf.maxPoolSize}")
	private int maxPoolSize;
	
	@Value("${async.conf.corePoolSize}")
	private int corePoolSize;
	
	@Value("${async.conf.queueCapacity}")
	private int queueCapacity;
	
	@Override
	public Executor getAsyncExecutor() {
		log.info("Creating ThreadPoolTaskExecutor with maxPoolSize:{}, corePoolSize:{}, queueCapacity:{}"
				, maxPoolSize
				, corePoolSize
				, queueCapacity);
		
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setTaskDecorator(new MdcTaskDecorator());
		executor.setMaxPoolSize(maxPoolSize);
		executor.setCorePoolSize(corePoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setThreadNamePrefix("BankAcctVerificationAsyncThread-");
		executor.initialize();
		
		return executor;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new GlobalExceptionHandler();
	}
	

}
