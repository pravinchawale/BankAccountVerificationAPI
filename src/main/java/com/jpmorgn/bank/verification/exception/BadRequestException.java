package com.jpmorgn.bank.verification.exception;

public class BadRequestException extends Exception{

	private static final long serialVersionUID = 4301615053454254039L;
	
	public BadRequestException() {
		super();
	}
	
	public BadRequestException(String exceptionMsg) {
		super(exceptionMsg);
	}
	
	public BadRequestException(String exceptionMsg, Exception e) {
		super(exceptionMsg, e);
	}
}
