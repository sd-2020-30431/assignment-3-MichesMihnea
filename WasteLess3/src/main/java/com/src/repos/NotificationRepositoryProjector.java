package com.src.repos;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import com.src.events.NotificationCreatedEvent;
import com.src.events.NotificationDeletedEvent;
import com.src.model.NotificationBean;
import com.src.queries.GetNotificationsQuery;

@Service
public class NotificationRepositoryProjector {

	private final NotificationRepository notificationRepository;

	public NotificationRepositoryProjector(NotificationRepository notificationRepository) {
		this.notificationRepository = notificationRepository;
	}

	@EventHandler
	public void addNotification(NotificationCreatedEvent event) throws Exception {
		NotificationEntity notification = new NotificationEntity();
		//notification.setNotificationId(event.getNotificationId());
		notification.setItemsAggregateId(event.getItemsAggregateId());
		notification.setMessage(event.getMessage());
		notificationRepository.save(notification);
		//System.out.println(notificationRepository.findAll());
	}
	
	
	@EventHandler
	public void deleteNotification(NotificationDeletedEvent event) throws Exception {
		notificationRepository.deleteById(event.getNotificationId());
	}

	@QueryHandler
	public List<NotificationBean> getNotifications(GetNotificationsQuery query) {
		System.out.println(notificationRepository.findAll() + " " + query.getItemsAggregateId());
		return notificationRepository.findByItemsAggregateId(query.getItemsAggregateId()).stream().map(toBook()).collect(Collectors.toList());
	}

	private Function<NotificationEntity, NotificationBean> toBook() {
		return e -> {
			NotificationBean notification = new NotificationBean();
			notification.setNotificationId(e.getNotificationId());
			notification.setMessage(e.getMessage());
			return notification;
		};
	}
}