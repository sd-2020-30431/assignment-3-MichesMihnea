package com.src.model;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import lombok.Data;

@Data
public class NotificationBean {
	private Integer notificationId;
	private String message;
}
