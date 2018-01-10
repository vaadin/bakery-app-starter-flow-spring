package com.vaadin.starter.bakery.ui.utils.messages;

public final class ErrorMessage {
	public static final String DB_NOT_FOUND_MESSAGE = "The selected entity was not found.";

	public static final String DB_CHANGES_MESSAGE = "Somebody else might have updated the data. Please refresh and try again.";

	public static final String DB_REFERENCES_MESSAGE = "The operation can not be executed as there are references to entity in the database";

	public static final String REQUIRED_MESSAGE = "Please fill out all required fields before proceeding.";

	private ErrorMessage() {
	}
}
