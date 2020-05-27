package com.src.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import lombok.Data;

@Data
public class RegisterNotificationCommand {
	@TargetAggregateIdentifier
	private final Integer itemsAggregateId;
	private final Integer notificationId;
	private final String message;

}
