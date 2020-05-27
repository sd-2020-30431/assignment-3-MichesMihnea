package com.src.repos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends CrudRepository<ItemEntity, Integer> {
	List<ItemEntity> findByItemsAggregateId(Integer itemsAggregateId);
}