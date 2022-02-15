package com.jpmorgn.bank.verification.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.jpmorgn.bank.verification.vo.DataSourceVO;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class BankVerificationConfig {
	
	@Value("${sources.connectionReqTimeout}") 
	private int connectionReqTimeout;
	
	@Value("${sources.connectTimeout}") 
	private int connectTimeout;
	
	/**
	 * List of available data source names. To be used as cache 
	 * 
	 * @param dataSourceConfig
	 * @return
	 */
	@Bean("availableDataSources")
	public List<String> availableDataSources(DataSourceConfig dataSourceConfig) {
		if (dataSourceConfig == null) {
			return Collections.emptyList();
		}
		
		List<String> availableDataSources = new ArrayList<>();
		for (DataSourceVO dataSourceVO : dataSourceConfig.getSources()) {
			availableDataSources.add(dataSourceVO.getName());
		}
		
		log.info(" -- Available data sources for bank account verification are : '{}'", availableDataSources);
		return availableDataSources;
	}
	
	/**
	 * Get RestTemplate bean for rest calls
	 * 
	 * @return
	 */
	@Bean
	public RestTemplate getRestTemplate() {
		log.info(" ++ Creating RestTemplate for data source call");
		return new RestTemplate(getHttpClientRequestFactory());
	}
	
	/**
	 * Returns HttpComponentsClientHttpRequestFactory for rest template
	 * 
	 * @return
	 */
	private HttpComponentsClientHttpRequestFactory getHttpClientRequestFactory() {
		log.info("Creating HttpComponentsClientHttpRequestFactory for rest template");
		
		PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
		manager.setValidateAfterInactivity(5000);
		
		RequestConfig.Builder rBuilder = RequestConfig.custom();
		rBuilder.setConnectionRequestTimeout(connectionReqTimeout);
		rBuilder.setConnectTimeout(connectTimeout);
		rBuilder.setCookieSpec(CookieSpecs.STANDARD);
		
		RequestConfig requestConfig = rBuilder.build();
		
		HttpClientBuilder hcBuilder = HttpClients.custom();
		hcBuilder.setConnectionManager(manager);
		hcBuilder.setConnectionManagerShared(true);
		hcBuilder.setDefaultRequestConfig(requestConfig);
		hcBuilder.setConnectionTimeToLive(5,  TimeUnit.SECONDS);
		
		return new HttpComponentsClientHttpRequestFactory(hcBuilder.build());
	}
}
