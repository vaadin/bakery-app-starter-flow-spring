package com.vaadin.starter.bakery.ui.messages;

public enum ErrorMessage {

	ENTITY_NOT_FOUND("The selected %s was not found.", true);

	private final String message;

	private final boolean persistent;

	private ErrorMessage(String message, boolean persistent) {
		this.message = message;
		this.persistent = persistent;
	}

	public String getMessage(Object... parameters) {
		return String.format(message, parameters);
	}

	public boolean isPersistent() {
		return persistent;
	}

}
