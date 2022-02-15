package com.jpmorgn.bank.verification.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDataSourceRequest {
	private String accountNumber;
}
