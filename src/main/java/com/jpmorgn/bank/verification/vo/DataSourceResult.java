package com.jpmorgn.bank.verification.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataSourceResult {
	
	private String source;
	private Boolean isValid;

}
