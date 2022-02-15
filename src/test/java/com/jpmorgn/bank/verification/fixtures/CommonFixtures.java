package com.jpmorgn.bank.verification.fixtures;

import java.util.ArrayList;
import java.util.List;

import com.jpmorgn.bank.verification.vo.BankAcctVerificationResult;
import com.jpmorgn.bank.verification.vo.BankVerificationRequest;
import com.jpmorgn.bank.verification.vo.DataSourceResult;

public class CommonFixtures {

	public BankVerificationRequest getBankVerificationRequest() {
		BankVerificationRequest request = new BankVerificationRequest();
		request.setAccountNumber("123456789");
		List<String> sources = new ArrayList<>();
		sources.add("source1");
		sources.add("source2");
		request.setSources(sources);
		
		return request;
	}
	
	public BankAcctVerificationResult getSuccessfulAcctVerificationResponse() {
		List<DataSourceResult> result = new ArrayList<>();
		BankAcctVerificationResult acctVerificationResult = new BankAcctVerificationResult();
		acctVerificationResult.setResult(result);
		
		return acctVerificationResult;
	}
	
}
