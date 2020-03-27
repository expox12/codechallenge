package com.codechallenge.jupiter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import com.codechallenge.TransactionsApplication;
import com.codechallenge.jupiter.models.Create;
import com.codechallenge.jupiter.models.Search;
import com.codechallenge.jupiter.models.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ContextConfiguration
@FieldDefaults(level = AccessLevel.PRIVATE)
@SpringBootTest(classes = TransactionsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionTest {
	
	@Autowired
	TestRestTemplate restTemplate;
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	
	@DisplayName("Test transaction status")
	@ParameterizedTest(name = "run #{index} with [{arguments}]")
	@MethodSource("com.codechallenge.jupiter.params.TransactionProvider#statusParams")
	public void statusTransaction(Status statusModel) 
			throws JSONException, JsonMappingException, JsonProcessingException {
		
		/* GIVEN */
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		final JSONObject transactionJsonObject = new JSONObject();
		transactionJsonObject.put("reference", statusModel.getReference());
		transactionJsonObject.put("channel", statusModel.getChannel());
		
		final HttpEntity<String> request = new HttpEntity<>(transactionJsonObject.toString(), headers);
		 
		/* WHEN */
		String transactionResultAsJsonStr = this.restTemplate.postForObject("/status", request, String.class);

		JsonNode root = this.objectMapper.readTree(transactionResultAsJsonStr);
		Assertions.assertNotNull(root);
		
		/* THEN */
		Assertions.assertEquals(statusModel.getStatus().name(), root.path("status").asText());
		
		/* THEN */
		Assertions.assertEquals(statusModel.getAmount(), root.path("amount").asText());
		
		/* THEN */
		Assertions.assertEquals(statusModel.getFee(), root.path("fee").asText());
		
		
	}
	
	@DisplayName("Test create transaction")
	@ParameterizedTest(name = "run #{index} with [{arguments}]")
	@MethodSource("com.codechallenge.jupiter.params.TransactionProvider#createParams")
	public void createTransaction(Create createModel) 
			throws JSONException, JsonMappingException, JsonProcessingException {
		
		/* GIVEN */
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		final JSONObject transactionJsonObject = new JSONObject();
		transactionJsonObject.put("reference", createModel.getReference());
		transactionJsonObject.put("iban", createModel.getIban());
		transactionJsonObject.put("date", createModel.getDate());
		transactionJsonObject.put("amount", createModel.getAmount());
		transactionJsonObject.put("fee", createModel.getFee());
		transactionJsonObject.put("description", createModel.getDescription());
		
		final HttpEntity<String> request = new HttpEntity<>(transactionJsonObject.toString(), headers);
		 
		/* WHEN */
		String transactionResultAsJsonStr = this.restTemplate.postForObject("/", request, String.class);

		JsonNode root = this.objectMapper.readTree(transactionResultAsJsonStr);
		Assertions.assertNotNull(root);
		
		/* THEN */
		final String reference = root.path("reference").asText();
		Assertions.assertTrue(reference != null && !reference.equals(""));		
		
	}
	
	@DisplayName("Test search transaction")
	@ParameterizedTest(name = "run #{index} with [{arguments}]")
	@MethodSource("com.codechallenge.jupiter.params.TransactionProvider #searchParams")
	public void searchTransaction(Search searchModel) 
			throws JSONException, JsonMappingException, JsonProcessingException {
		
		/* GIVEN */
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		final JSONObject transactionJsonObject = new JSONObject();
		transactionJsonObject.put("iban", searchModel.getIban());
		transactionJsonObject.put("sort", searchModel.getSort());
		
		final HttpEntity<String> request = new HttpEntity<>(transactionJsonObject.toString(), headers);
		 
		/* WHEN */
		String transactionResultAsJsonStr = this.restTemplate.postForObject("/search", request, String.class);
		
		/* THEN */
		final JSONArray transactionsList = new JSONArray(transactionResultAsJsonStr);
		Assertions.assertTrue(transactionsList.length() > 0);
		
	}
	

}
