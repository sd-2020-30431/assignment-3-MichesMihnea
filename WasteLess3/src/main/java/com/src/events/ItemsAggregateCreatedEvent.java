package com.src.events;

import lombok.Data;

@Data
public class ItemsAggregateCreatedEvent {
	private final Integer itemsAggregateId;
	private final String name;
}