package com.jpmorgn.bank.verification.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.jpmorgn.bank.verification.config.DataSourceConfig;
import com.jpmorgn.bank.verification.exception.BadRequestException;
import com.jpmorgn.bank.verification.service.BankAccountVerificationService;
import com.jpmorgn.bank.verification.vo.BankAcctVerificationResult;
import com.jpmorgn.bank.verification.vo.BankVerificationRequest;
import com.jpmorgn.bank.verification.vo.DataSourceResult;
import com.jpmorgn.bank.verification.vo.DataSourceVO;

import lombok.extern.slf4j.Slf4j;

/**
 * Business logic which takes care of bank account verification 
 * 
 * @author pravin
 *
 */
@Component
@Slf4j
public class BankVerificationBO {

	@Autowired
	private DataSourceConfig dataSourceConfig;
	
	@Autowired
	private BankAccountVerificationService BankAccountVerificationService;
	
	@Autowired
	@Qualifier("availableDataSources")
	private List<String> availableDataSources;
	
	/**
	 * Validates data source names from input to find invalid data source names 
	 * that are passed on input not defined in property files
	 * 
	 * @param request
	 * @throws BadRequestException
	 */
	public void validateInputDataSources(BankVerificationRequest request) throws BadRequestException {
		log.debug("Validating data source names from input requrest");
		
		if (CollectionUtils.isEmpty(request.getSources())) {
			return;
		}
		
		for (String dataSource : request.getSources()) {
			if (!availableDataSources.contains(dataSource)) {
				throw new BadRequestException("Invalid source '"
						+ dataSource 
						+ "' passed on input. Valid data source names are: "
						+ availableDataSources);
			}
		}
	}
	
	/**
	 * Returns URL based on Data source name
	 * 
	 * @param name
	 * @return
	 */
	public String getDataSourceURL(String name) {
		for (DataSourceVO dataSourceVO : dataSourceConfig.getSources()) {
			if (name.equals(dataSourceVO.getName())) {
				return dataSourceVO.getUrl();
			}
		}
		
		return null;
	}
	
	/**
	 * Business logic to verify bank account number
	 *  - Validates the data source names from request
	 *  - Gets the url of data source from defined configuration
	 *  - Calls data sources in parallel/asynchronously
	 *  - Combines the result from all data sources
	 *  - Returns final verification response
	 *  
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	public BankAcctVerificationResult verifyAccount(BankVerificationRequest request) throws Exception {
		log.debug("Executing business logic for bank account verification");
		
		validateInputDataSources(request);
		
		List<String> dataSourcesToUse = CollectionUtils.isEmpty(request.getSources()) ? availableDataSources : request.getSources();
		
		Map<String, CompletableFuture<Boolean>> dataSourceResponses = new HashMap<>();
		for (String dataSource : dataSourcesToUse) {
			String url = getDataSourceURL(dataSource);
			dataSourceResponses.put(dataSource, BankAccountVerificationService.verifyWithDataService(url, request.getAccountNumber()));
		}
		
		BankAcctVerificationResult bankAcctVerificationResult = BankAcctVerificationResult
				.builder()
				.result(new ArrayList<>())
				.build();
		
		for (String dataSource : dataSourceResponses.keySet()) {
			DataSourceResult dataSourceResult = DataSourceResult
					.builder()
					.source(dataSource)
					.isValid(dataSourceResponses.get(dataSource).get())
					.build();
			
			bankAcctVerificationResult.getResult().add(dataSourceResult);
		}
		
		return bankAcctVerificationResult;
	}
}
