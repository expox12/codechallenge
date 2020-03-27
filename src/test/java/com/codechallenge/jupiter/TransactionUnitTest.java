package com.codechallenge.jupiter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.codechallenge.TransactionsApplication;
import com.codechallenge.domains.Transaction;
import com.codechallenge.exceptions.NoAccountException;
import com.codechallenge.exceptions.NoFundsException;
import com.codechallenge.models.SearchRequest;
import com.codechallenge.models.TransactionRequest;
import com.codechallenge.repositories.AccountRepository;
import com.codechallenge.repositories.TransactionRepository;
import com.codechallenge.services.TransactionServiceImpl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;


@FieldDefaults(level = AccessLevel.PRIVATE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = TransactionsApplication.class)
@ExtendWith(SpringExtension.class)
class TransactionUnitTest {
	
	TransactionServiceImpl service;
	
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	TransactionRepository transactionRepository;	
	
	
	@BeforeEach
	void initTest() {
		this.service = new TransactionServiceImpl(transactionRepository, accountRepository);
	}
	
	@DisplayName("Find a transaction by IBAN")
	@ParameterizedTest(name = "${index} IBAN -> {arguments}")
	@ValueSource(strings = { "ES4131891613999502318057", "ES7720758605524241614429" })
	void findATransactionByIban(String iban) {
		SearchRequest request = new SearchRequest();
		request.setIban(iban);
		request.setSort(Direction.ASC);
		
		List<Transaction> transactionList = this.service.findTransactions(request);
		Assertions.assertTrue(transactionList.size() > 0);
	}
	
	@DisplayName("Find a transaction by empty IBAN")
	@ParameterizedTest(name = "Empty argument")
	@ValueSource(strings = { "" })
	void findATransactionByEmptyIban(String iban) {
		SearchRequest request = new SearchRequest();
		request.setIban(iban);
		request.setSort(Direction.ASC);
		
		List<Transaction> transactionList = this.service.findTransactions(request);
		Assertions.assertTrue(transactionList.isEmpty());
	}
	
	
	
	@Nested
	class ExceptionsTest {
		
		@DisplayName("Throws an AccountException on create Transaction")
		@ParameterizedTest(name = "No account found with IBAN -> {arguments}")
		@ValueSource(strings = { "ES0000000000000000000000", "ES1111111111111111111111" })
		public void noAccountException(String iban) {
				
			TransactionRequest request = new TransactionRequest(
					"456111", 
					iban,
					ZonedDateTime.now(),
					new BigDecimal("100"), 
					new BigDecimal("2.3"), 
					"Gas"
			);
			Assertions.assertThrows(NoAccountException.class, () -> {
				service.createTransaction(request);
			});
		}
		
		@DisplayName("Throws an NoFundsException on create Transaction")
		@ParameterizedTest(name = "Invalid fields: amount({0}), fee({1})")
		@CsvSource({ "-10, 1", "5, 8" })
		public void NoFundsException(String amount, String fee) {
			BigDecimal amountDecimal = new BigDecimal(amount);
			BigDecimal feeDecimal = new BigDecimal(fee);
			
			Assumptions.assumeTrue(amountDecimal.doubleValue() < feeDecimal.doubleValue());
			TransactionRequest request = new TransactionRequest(
					"456111", 
					"ES9121000418450200051332",
					ZonedDateTime.now(),
					amountDecimal, 
					feeDecimal, 
					"Gas"
			);
			
			Assertions.assertThrows(NoFundsException.class, () -> {
				service.createTransaction(request);
			});
		}
		
		
	}	

}
