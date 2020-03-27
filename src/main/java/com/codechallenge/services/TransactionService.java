package com.codechallenge.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codechallenge.domains.Transaction;
import com.codechallenge.models.SearchRequest;
import com.codechallenge.models.StatusRequest;
import com.codechallenge.models.StatusResponse;
import com.codechallenge.models.TransactionRequest;
import com.codechallenge.models.TransactionResponse;

public interface TransactionService {
	
	static final Logger LOG = LoggerFactory.getLogger(TransactionService.class);
	
	static final String DEFAULT_SORT_PROPERTY = "amount";
	static final int REFERENCE_LENGTH = 6;
	
	
	TransactionResponse createTransaction(final TransactionRequest transactionRequest);
	
	List<Transaction> findTransactions(final SearchRequest searchRequest);
	
	StatusResponse findTransactionStatus(final StatusRequest statusRequest);

}
