package com.src.events;

import java.util.Date;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Data;

@Data
public class NotificationDeletedEvent {
	@TargetAggregateIdentifier
	private final Integer itemsAggregateId;
	private final Integer notificationId;
}