package com.jpmorgn.bank.verification.helper;

import static com.jpmorgn.bank.verification.constant.BankAccountVerificationConstant.APP_SLA_IN_MILLIS;
import static com.jpmorgn.bank.verification.constant.BankAccountVerificationConstant.BANK_ACCT;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import lombok.extern.slf4j.Slf4j;

/**
 * Helper class for logger once transaction is completed/failed/response SLA missed etc.
 * 
 * @author pravin
 *
 */
@Slf4j
@Component
public class LogHelper {
	
	/**
	 * Logs the transaction completion message in log for audit/troubleshooting based on response time 
	 * 
	 * @param bankAccount
	 * @param stopWatch
	 */
	public void logTransactionDetails(String bankAccount, StopWatch stopWatch) {
		stopWatch.stop();
		if (stopWatch.getTotalTimeMillis() <= APP_SLA_IN_MILLIS) {
			log.info("The verification process completed for bank account '{}'. Total response time: {} ms"
					, bankAccount
					, stopWatch.getTotalTimeMillis());
		} else {
			log.warn("The verification process completed for bank account '{}'. Response SLA missed. Total response time: {} ms"
					, bankAccount
					, stopWatch.getTotalTimeMillis());
		}
	}
	
	/**
	 * Logs the error message
	 * 
	 * @param bankAccount
	 * @param e
	 */
	public void logErrorTransaction(String bankAccount, Throwable e) {
		log.warn("Unable to verify bank account '{}'. Exception: {}"
				, bankAccount
				, e.getMessage()
				, e);
	}
	
	/**
	 * Logs the error message
	 * 
	 * @param e
	 */
	public void logErrorTransaction(Throwable e) {
		logErrorTransaction(MDC.get(BANK_ACCT), e);
	}
}
