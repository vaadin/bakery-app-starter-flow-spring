package com.vaadin.starter.bakery.backend.data.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.vaadin.starter.bakery.backend.data.OrderState;

@Entity
public class HistoryItem extends AbstractEntity {

	private OrderState newState;

	@NotEmpty
	@NotBlank
	@Size(max = 255)
	private String message;

	@NotNull
	private LocalDateTime timestamp;
	@ManyToOne
	@NotNull
	private User createdBy;

	HistoryItem() {
		// Empty constructor is needed by Spring Data / JPA
	}

	public HistoryItem(User createdBy, String message) {
		this.createdBy = createdBy;
		this.message = message;
		timestamp = LocalDateTime.now();
	}

	public OrderState getNewState() {
		return newState;
	}

	public void setNewState(OrderState newState) {
		this.newState = newState;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

}
