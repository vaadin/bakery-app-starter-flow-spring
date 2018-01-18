package com.vaadin.starter.bakery.ui.view;

import com.vaadin.flow.component.HasElement;

/**
 * 
 * Unidirectional (server -> client) companion for client-side HasNotificationsMixin.
 *
 */
public interface HasNotifications extends HasElement {

	default void showNotification(String message) {
		showNotification(message, false);
	}

	default void showNotification(String message, boolean persistent) {
		getElement().callFunction("showNotification", message, persistent);
	}
}
