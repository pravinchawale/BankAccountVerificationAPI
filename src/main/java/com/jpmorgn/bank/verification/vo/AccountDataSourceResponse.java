package com.jpmorgn.bank.verification.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AccountDataSourceResponse {
	@JsonProperty("isValid")
	private boolean isValid;	
}
