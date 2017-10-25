package com.vaadin.starter.bakery.ui;

import com.vaadin.ui.common.HasElement;

/**
 * 
 * Unidirectional (server -> client) companion for client-side NotificationsMixin.
 *
 */
public interface HasToast extends HasElement {

	default void toast(String message) {
		toast(message, false);
	}

	default void toast(String message, boolean persistent) {
		getElement().callFunction("displayToast", message, persistent);
	}
}
