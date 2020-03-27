package com.codechallenge.jupiter.models;

import com.codechallenge.models.enums.TransactionChannel;
import com.codechallenge.models.enums.TransactionStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Status {
	String reference;
	TransactionChannel channel;
	TransactionStatus status;
	String amount, fee;
}
