package com.business;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.src.aggregate.ItemsAggregate;
import com.src.commands.DeleteItemCommand;
import com.src.commands.DeleteNotificationCommand;
import com.src.commands.RegisterItemCommand;
import com.src.commands.RegisterItemsAggregateCommand;
import com.src.commands.RegisterNotificationCommand;
import com.src.model.ItemBean;
import com.src.model.ItemsAggregateBean;
import com.src.model.NotificationBean;
import com.src.queries.GetItemsQuery;
import com.src.queries.GetItemsAggregateQuery;
import com.src.queries.GetNotificationsQuery;


@RestController
public class WastelessController {

	private CommandGateway commandGateway;
	private QueryGateway queryGateway;

	@Autowired
	public WastelessController(CommandGateway commandGateway, QueryGateway queryGateway) {
		this.commandGateway = commandGateway;
		this.queryGateway = queryGateway;
	}

	public String addItemsAggregate(ItemsAggregateBean itemsAggregate) {
		commandGateway.send(new RegisterItemsAggregateCommand(itemsAggregate.getItemsAggregateId(), itemsAggregate.getName()));
		return "Saved";
	}

	public ItemsAggregate getItemsAggregate(Integer itemsAggregate) throws InterruptedException, ExecutionException {
		CompletableFuture<ItemsAggregate> future = queryGateway.query(new GetItemsAggregateQuery(itemsAggregate), ItemsAggregate.class);
		return future.get();
	}

	public String addItem(Integer itemsAggregate, ItemBean item) {
		commandGateway.send(new RegisterItemCommand(itemsAggregate, item.getItemId(), item.getName(), item.getCalories(),
				item.getQuantity(), item.getPurchase(), item.getExpiration()));
		return "Saved";
	}
	
	public String addNotification(Integer itemsAggregate, NotificationBean notification) {
		commandGateway.send(new RegisterNotificationCommand(itemsAggregate, notification.getNotificationId(), notification.getMessage()));
		return "Saved";
	}
	
	public String deleteItem(Integer itemsAggregate, Integer itemId) {
		commandGateway.send(new DeleteItemCommand(itemsAggregate, itemId));
		return "Deleted";
	}
	
	public String deleteNotification(Integer itemsAggregate, Integer notificationId) {
		commandGateway.send(new DeleteNotificationCommand(itemsAggregate, notificationId));
		return "Deleted";
	}

	public List<ItemBean> getItems(Integer itemsAggregate) throws InterruptedException, ExecutionException {
		return queryGateway.query(new GetItemsQuery(itemsAggregate), ResponseTypes.multipleInstancesOf(ItemBean.class)).get();
	}
	
	public List<NotificationBean> getNotifications(Integer itemsAggregate) throws InterruptedException, ExecutionException {
		return queryGateway.query(new GetNotificationsQuery(itemsAggregate), ResponseTypes.multipleInstancesOf(NotificationBean.class)).get();
	}

}
