package com.src.commands;

import java.util.Date;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Data;

@Data
public class RegisterItemCommand {
	@TargetAggregateIdentifier
	private final Integer itemsAggregateId;
	private final Integer itemId;
	private final String name;
	private final Integer calories;
	private final Integer quantity;
	private final Date purchase;
	private final Date expiration;
}