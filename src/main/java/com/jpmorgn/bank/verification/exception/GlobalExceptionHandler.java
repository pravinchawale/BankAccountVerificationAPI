package com.jpmorgn.bank.verification.exception;

import static com.jpmorgn.bank.verification.constant.BankAccountVerificationConstant.BANK_ACCT;

import java.lang.reflect.Method;

import org.jboss.logging.MDC;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.jpmorgn.bank.verification.helper.LogHelper;
import com.jpmorgn.bank.verification.vo.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler implements AsyncUncaughtExceptionHandler{
	private final static String BAD_REQUEST_MSG = "Bad request";
	private final static String UNABLE_TO_PROCESS_MSG = "Unable to process request";
	private final static String METHOD_ARG_NOT_VALID = "Input request validation exception";
	
	@Autowired
	private LogHelper logHelper;
	
	/**
	 * Global exception handler for BadRequestException
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e) {
		ErrorResponse errorResponse = ErrorResponse.builder().errorMessage(BAD_REQUEST_MSG).build();
		logHelper.logErrorTransaction(e);
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Global exception handler for MethodArgumentNotValidException
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		ErrorResponse errorResponse = ErrorResponse.builder().errorMessage(METHOD_ARG_NOT_VALID).build();
		logHelper.logErrorTransaction(e);
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Global exception handler for RuntimeException
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
		ErrorResponse errorResponse = ErrorResponse.builder().errorMessage(UNABLE_TO_PROCESS_MSG).build();
		logHelper.logErrorTransaction(e);
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public void handleUncaughtException(Throwable ex, Method method, Object... params) {
		log.error("Execution occurred while running in async mode for method:{}, bankAccount:{}", method.getName(), MDC.get(BANK_ACCT), ex);
	}
}
