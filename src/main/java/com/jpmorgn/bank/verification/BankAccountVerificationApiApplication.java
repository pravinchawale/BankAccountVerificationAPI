package com.jpmorgn.bank.verification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BankAccountVerificationApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankAccountVerificationApiApplication.class, args);
	}

}
