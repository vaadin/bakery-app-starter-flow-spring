package com.vaadin.starter.bakery.ui;

import com.vaadin.flow.router.View;

/**
 * Unidirectional (server -> client) companion for client-side NotificationsMixin.
 *
 * @author Vaadin Ltd
 */
public interface HasToast extends View {

	default void toast(String message) {
		toast(message, false);
	}

	default void toast(String message, boolean persistent) {
		getElement().callFunction("displayToast", message, persistent);
	}
}
