package com.jpmorgn.bank.verification.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpmorgn.bank.verification.bo.BankVerificationBO;
import com.jpmorgn.bank.verification.fixtures.CommonFixtures;
import com.jpmorgn.bank.verification.helper.LogHelper;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class BankVerificationControllerTest extends CommonFixtures {
	
	@InjectMocks
	@Spy
	private BankVerificationController bankVerificationController;
	
	@Mock
	private LogHelper logHelper;
	
	@Mock
	private BankVerificationBO bankVerificationBO;
	
	private MockMvc mockMvc;
	
	@SuppressWarnings("deprecation")
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(bankVerificationController).build();
	}
	
	@Test
	public void healthCheckTest() throws Exception {
		MvcResult result = mockMvc.perform(get("/bank/account/v1/healthcheck"))
				.andExpect(status().is2xxSuccessful()).andReturn();
		assertNotNull(result.getResponse().getContentAsString());
	}
	
	@Test
	public void verifyAccountTest() throws Exception {
		Mockito.when(bankVerificationBO.verifyAccount(Mockito.any())).thenReturn(getSuccessfulAcctVerificationResponse());
		ObjectMapper mapper = new ObjectMapper();
		byte[] inputByteArray = mapper.writeValueAsBytes(getBankVerificationRequest());
		MvcResult result = mockMvc.perform(
				post("/bank/account/v1/verify")
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputByteArray))
				.andExpect(status().is2xxSuccessful())
				.andReturn();
		assertNotNull(result.getResponse().getContentAsString());
	}

}
