package com.src.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Data;

@Data
public class DeleteItemCommand {
	@TargetAggregateIdentifier
	private final Integer itemsAggregateId;
	private final Integer itemId;

}
