package com.src.repos;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class ItemEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer itemId;
	@Column
	private int itemsAggregateId;
	@Column
	private String name;
	@Column
	private Integer calories;
	@Column
	private Integer quantity;
	@Column
	private Date purchase;
	@Column
	private Date expiration;
}