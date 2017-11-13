package com.vaadin.starter.bakery.backend.data;

import java.util.Locale;

import com.vaadin.shared.util.SharedUtil;

public enum OrderState {
	NEW, CONFIRMED, READY, DELIVERED, PROBLEM, CANCELLED;

	/**
	 * Gets a version of the enum identifier in a human friendly format.
	 *
	 * @return a human friendly version of the identifier
	 */
	public String toString() {
		return SharedUtil.capitalize(name().toLowerCase(Locale.ENGLISH));
	}
}
