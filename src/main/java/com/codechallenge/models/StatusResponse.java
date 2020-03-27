package com.codechallenge.models;

import java.io.Serializable;
import java.math.BigDecimal;

import com.codechallenge.models.enums.TransactionStatus;
import com.codechallenge.serialization.MoneySerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiModel(value = "Transaction Status", description = "Status and additional information for a specific transaction")
public class StatusResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9216934759798217126L;

	@NonNull
	@ApiModelProperty(value = "The transaction reference number")
	String reference;

	@ApiModelProperty(value = "The status of the transaction", allowableValues = "PENDING,SETTLED,FUTURE,INVALID")
	TransactionStatus status;

	@JsonSerialize(using = MoneySerializer.class)
	@ApiModelProperty(value = "The amount of the transaction")
	BigDecimal amount;

	@JsonSerialize(using = MoneySerializer.class)
	@ApiModelProperty(value = "The fee applied to the transaction")
	BigDecimal fee;
}
