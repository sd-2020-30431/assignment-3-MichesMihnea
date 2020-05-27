package com.src.model;

import java.util.Date;

import lombok.Data;

@Data
public class ItemBean {
	private Integer itemId;
	private String name;
	private Integer calories;
	private Integer quantity;
	private Date purchase;
	private Date expiration;
}