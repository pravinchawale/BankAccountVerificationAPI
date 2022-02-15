package com.jpmorgn.bank.verification.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankAcctVerificationResult {
	private List<DataSourceResult> result;
}
