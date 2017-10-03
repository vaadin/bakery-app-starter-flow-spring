package com.vaadin.starter.bakery.ui.components;

import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.ui.HasToast;
import com.vaadin.starter.bakery.ui.messages.Message;

public interface EntityView extends HasToast, HasLogger {

	void closeDialog(boolean updated);

	void confirm(Message message, Runnable operation);

	void openDialog();

	default void showError(String message) {
		toast(message, false);
	}
}
