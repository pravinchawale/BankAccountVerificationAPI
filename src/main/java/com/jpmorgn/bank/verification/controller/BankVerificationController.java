package com.jpmorgn.bank.verification.controller;

import static com.jpmorgn.bank.verification.constant.BankAccountVerificationConstant.BANK_ACCT;

import javax.validation.Valid;

import org.jboss.logging.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jpmorgn.bank.verification.bo.BankVerificationBO;
import com.jpmorgn.bank.verification.helper.LogHelper;
import com.jpmorgn.bank.verification.vo.BankAcctVerificationResult;
import com.jpmorgn.bank.verification.vo.BankVerificationRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * Rest controller for bank account verification
 * 
 * @author pravin
 *
 */

@RestController
@RequestMapping("/bank/account/v1")
@Slf4j
public class BankVerificationController{

	@Autowired
	private BankVerificationBO bankVerificationBO;
	
	@Autowired
	private LogHelper logHelper;
	
	/**
	 * Health check method 
	 * 
	 * @return
	 */
	@GetMapping("/healthcheck")
	public ResponseEntity<String> healthCheck() {
		return new ResponseEntity<String>("Bank account verification API is up and running", HttpStatus.OK);
	}
	
	/**
	 * Controller method for handling bank account verification requests
	 * 
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@PostMapping(value = "/verify", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BankAcctVerificationResult> verifyAccount(@Valid @RequestBody BankVerificationRequest request) throws Exception {
		log.debug("Got request for bank account verification..");
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		MDC.clear();
		MDC.put(BANK_ACCT, request.getAccountNumber());
		
		BankAcctVerificationResult response =  bankVerificationBO.verifyAccount(request);
		logHelper.logTransactionDetails(request.getAccountNumber(), stopWatch);
		
		return new ResponseEntity<BankAcctVerificationResult>(response, HttpStatus.OK);
	}
}
