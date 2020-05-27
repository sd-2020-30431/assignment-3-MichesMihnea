package com.src.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Data;

@Data
public class RegisterItemsAggregateCommand {
	@TargetAggregateIdentifier
	private final Integer itemsAggregateId;

	private final String name;
}