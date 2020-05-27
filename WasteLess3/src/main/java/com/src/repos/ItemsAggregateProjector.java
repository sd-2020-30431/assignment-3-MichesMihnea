package com.src.repos;



import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.axonframework.modelling.command.Repository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.src.aggregate.ItemsAggregate;
import com.src.queries.GetItemsAggregateQuery;

@Service
public class ItemsAggregateProjector {
	private final Repository<ItemsAggregate> libraryRepository;

	public ItemsAggregateProjector(Repository<ItemsAggregate> libraryRepository) {
		this.libraryRepository = libraryRepository;
	}

	@QueryHandler
	public ItemsAggregate getLibrary(GetItemsAggregateQuery query) throws InterruptedException, ExecutionException {
		CompletableFuture<ItemsAggregate> future = new CompletableFuture<ItemsAggregate>();
		libraryRepository.load("" + query.getItemsAggregateId()).execute(future::complete);
		return future.get();
	}

}