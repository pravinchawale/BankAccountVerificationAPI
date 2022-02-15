package com.jpmorgn.bank.verification.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jpmorgn.bank.verification.vo.AccountDataSourceRequest;
import com.jpmorgn.bank.verification.vo.AccountDataSourceResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * Service layer for making service calls to data sources for bank account verification
 * 
 * @author pravin
 *
 */

@Service
@Slf4j
public class BankAccountVerificationService {

	@Autowired
	private RestTemplate restTemplate;
	
	/**
	 * Asynchronously calls data sources/rest endpoints for bank account verification
	 * 
	 * @param url
	 * @param bankAccount
	 * @return
	 * @throws Exception 
	 */
	@Async
	public CompletableFuture<Boolean> verifyWithDataService(String url, String bankAccount) throws Exception {
		log.debug("Calling data source with url '{}' for bank verification", url);
		
		AccountDataSourceRequest accountDSRequest = AccountDataSourceRequest
				.builder()
				.accountNumber(bankAccount)
				.build();
		
		ResponseEntity<AccountDataSourceResponse> accountDSResponse = null;
		try {
			accountDSResponse = restTemplate.postForEntity(url, accountDSRequest, AccountDataSourceResponse.class);
		} catch (Exception e) {
			throw e;
		}
		
		return CompletableFuture.completedFuture(accountDSResponse.getBody().isValid());
	}
}
