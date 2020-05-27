package com.src.repos;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import com.src.events.ItemCreatedEvent;
import com.src.events.ItemDeletedEvent;
import com.src.model.ItemBean;
import com.src.queries.GetItemsQuery;

@Service
public class ItemRepositoryProjector {

	private final ItemRepository itemRepository;

	public ItemRepositoryProjector(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	@EventHandler
	public void addItem(ItemCreatedEvent event) throws Exception {
		ItemEntity item = new ItemEntity();
		//item.setItemId(event.getItemId());
		item.setItemsAggregateId(event.getItemsAggregateId());
		item.setName(event.getName());
		item.setCalories(event.getCalories());
		item.setQuantity(event.getQuantity());
		item.setPurchase(event.getPurchase());
		item.setExpiration(event.getExpiration());
		itemRepository.save(item);
		//System.out.println(itemRepository.findAll());
	}
	
	@EventHandler
	public void deleteItem(ItemDeletedEvent event) throws Exception {
		itemRepository.deleteById(event.getItemId());
	}

	@QueryHandler
	public List<ItemBean> getBooks(GetItemsQuery query) {
		System.out.println(itemRepository.findAll() + " " + query.getItemsAggregateId());
		return itemRepository.findByItemsAggregateId(query.getItemsAggregateId()).stream().map(toBook()).collect(Collectors.toList());
	}

	private Function<ItemEntity, ItemBean> toBook() {
		return e -> {
			ItemBean item = new ItemBean();
			item.setItemId(e.getItemId());
			item.setName(e.getName());
			item.setCalories(e.getCalories());
			item.setQuantity(e.getQuantity());
			item.setPurchase(e.getPurchase());
			item.setExpiration(e.getExpiration());
			return item;
		};
	}
}