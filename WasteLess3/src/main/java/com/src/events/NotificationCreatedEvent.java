package com.src.events;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import lombok.Data;

@Data
public class NotificationCreatedEvent {
	@TargetAggregateIdentifier
	private final Integer itemsAggregateId;
	private final Integer notificationId;
	private final String message;
}
