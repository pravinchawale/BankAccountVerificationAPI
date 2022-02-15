package com.jpmorgn.bank.verification.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.jpmorgn.bank.verification.vo.DataSourceVO;

import lombok.Data;

/**
 * This configuration class will be used to read the data source properties i.e. name and url
 * 
 * @author pravin
 *
 */

@Data
@Component
@ConfigurationProperties
public class DataSourceConfig {
	List<DataSourceVO> sources;
}
