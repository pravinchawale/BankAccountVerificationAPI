package com.jpmorgn.bank.verification;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jpmorgn.bank.verification.controller.BankVerificationController;

@SpringBootTest
class SmokeTest {

	@Autowired
	private BankVerificationController bankVerificationController;
	
	@Test
	void contextLoads() {
		assertThat(bankVerificationController).isNotNull();
	}
}
