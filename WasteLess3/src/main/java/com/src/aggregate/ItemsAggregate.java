package com.src.aggregate;

import java.util.ArrayList;
import java.util.List;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.util.Assert;

import com.src.commands.DeleteItemCommand;
import com.src.commands.DeleteNotificationCommand;
import com.src.commands.RegisterItemCommand;
import com.src.commands.RegisterItemsAggregateCommand;
import com.src.commands.RegisterNotificationCommand;
import com.src.events.ItemCreatedEvent;
import com.src.events.ItemDeletedEvent;
import com.src.events.ItemsAggregateCreatedEvent;
import com.src.events.NotificationCreatedEvent;
import com.src.events.NotificationDeletedEvent;

@Aggregate
public class ItemsAggregate {

	@AggregateIdentifier
	private Integer itemsAggregateId;

	private String name;

	private List<Integer> itemIds;
	
	private List<Integer> notificationIds;

	protected ItemsAggregate() {
		// For Axon instantiation
	}

	@CommandHandler
	public ItemsAggregate(RegisterItemsAggregateCommand cmd) {
		Assert.notNull(cmd.getItemsAggregateId(), "ID should not be null");
		Assert.notNull(cmd.getName(), "Name should not be null");

		AggregateLifecycle.apply(new ItemsAggregateCreatedEvent(cmd.getItemsAggregateId(), cmd.getName()));
	}

	public Integer getLibraryId() {
		return itemsAggregateId;
	}

	public String getName() {
		return name;
	}

	public List<Integer> getItemIds() {
		return itemIds;
	}
	
	public List<Integer> getNotificationIds() {
		return notificationIds;
	}

	@CommandHandler
	public void addItem(RegisterItemCommand cmd) {
		Assert.notNull(cmd.getItemsAggregateId(), "ID should not be null");
		//Assert.notNull(cmd.getItemId(), "Book ISBN should not be null");

		AggregateLifecycle.apply(new ItemCreatedEvent(cmd.getItemsAggregateId(), cmd.getItemId(), cmd.getName(), cmd.getCalories(),
				cmd.getQuantity(), cmd.getPurchase(), cmd.getExpiration()));
	}
	
	@CommandHandler
	public void addNotification(RegisterNotificationCommand cmd) {
		Assert.notNull(cmd.getItemsAggregateId(), "ID should not be null");
		AggregateLifecycle.apply(new NotificationCreatedEvent(cmd.getItemsAggregateId(), cmd.getNotificationId(), cmd.getMessage()));
	}
	
	@CommandHandler
	public void deleteItem(DeleteItemCommand cmd) {
		Assert.notNull(cmd.getItemsAggregateId(), "ID should not be null");
		//Assert.notNull(cmd.getItemId(), "Book ISBN should not be null");

		AggregateLifecycle.apply(new ItemDeletedEvent(cmd.getItemsAggregateId(), cmd.getItemId()));
	}
	
	@CommandHandler
	public void deleteNotification(DeleteNotificationCommand cmd) {
		Assert.notNull(cmd.getItemsAggregateId(), "ID should not be null");
		//Assert.notNull(cmd.getItemId(), "Book ISBN should not be null");

		AggregateLifecycle.apply(new NotificationDeletedEvent(cmd.getItemsAggregateId(), cmd.getNotificationId()));
	}

	@EventSourcingHandler
	private void handleCreatedEvent(ItemsAggregateCreatedEvent event) {
		itemsAggregateId = event.getItemsAggregateId();
		name = event.getName();
		itemIds = new ArrayList<>();
		notificationIds = new ArrayList<>();
	}

	@EventSourcingHandler
	private void addBook(ItemCreatedEvent event) {
		itemIds.add(event.getItemId());
	}
	
	@EventSourcingHandler
	private void addNotification(NotificationCreatedEvent event) {
		notificationIds.add(event.getNotificationId());
	}

}