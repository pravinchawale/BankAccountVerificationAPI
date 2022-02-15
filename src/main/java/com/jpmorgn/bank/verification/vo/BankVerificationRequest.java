package com.jpmorgn.bank.verification.vo;

import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankVerificationRequest {
	
	@NotBlank
	private String accountNumber;
	private List<String> sources;
}
