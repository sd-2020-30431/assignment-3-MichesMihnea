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
public class NotificationEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer notificationId;
	@Column
	private int itemsAggregateId;
	@Column
	private String message;
}
