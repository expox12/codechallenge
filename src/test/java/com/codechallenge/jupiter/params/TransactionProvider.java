package com.codechallenge.jupiter.params;

import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

import com.codechallenge.jupiter.models.Create;
import com.codechallenge.jupiter.models.Search;
import com.codechallenge.jupiter.models.Status;
import com.codechallenge.models.enums.TransactionChannel;
import com.codechallenge.models.enums.TransactionStatus;

class TransactionProvider {
	
	public static Stream<Arguments> statusParams() {
	    return Stream.of(
	    		Arguments.of(new Status("xxxxxx", TransactionChannel.ATM, 		TransactionStatus.INVALID, "",  	"")),
	    		Arguments.of(new Status("xxxxxx", TransactionChannel.CLIENT,	TransactionStatus.INVALID, "",  	"")),
	    		Arguments.of(new Status("xxxxxx", TransactionChannel.INTERNAL, 	TransactionStatus.INVALID, "",  	"")),
	    		Arguments.of(new Status("456781", TransactionChannel.INTERNAL, 	TransactionStatus.SETTLED, "330.2", "3.6")),
	    		Arguments.of(new Status("456782", TransactionChannel.INTERNAL, 	TransactionStatus.PENDING, "25.4",  "2.8"))
	    );
	}
	
	public static Stream<Arguments> createParams() {
	    return Stream.of(
	    		Arguments.of(new Create("456111", "ES9121000418450200051332", "2021-01-16T20:47:41.000Z", "100", "2.3", "Gas")),
	    		Arguments.of(new Create("456112", "ES3120713285602792521178", "2018-02-16T13:47:41.000Z", "25", "2.3", "Books")),
	    		Arguments.of(new Create("456113", "ES8520865224136640176203", "2020-03-10T09:47:41.000Z", "12", "1.2", "Internet"))
	    );
	}
	
	public static Stream<Arguments> searchParams() {
	    return Stream.of(
	    		Arguments.of(new Search("ES4131891613999502318057", "ASC")),
	    		Arguments.of(new Search("ES7720758605524241614429", "DESC"))
	    );
	}
	
}
