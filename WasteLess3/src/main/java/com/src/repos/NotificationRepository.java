package com.src.repos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends CrudRepository<NotificationEntity, Integer> {
	List<NotificationEntity> findByItemsAggregateId(Integer itemsAggregateId);
}