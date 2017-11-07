package com.vaadin.starter.bakery.backend.data;

public enum OrderState {
	NEW, CONFIRMED, READY, DELIVERED, PROBLEM, CANCELLED;

	/**
	 * Gets a version of the enum identifier in a human friendly format.
	 *
	 * @return a human friendly version of the identifier
	 */
	public String getDisplayName() {
		return StringUtil.upperCaseUnderscoreToHumanFriendly(name());
	}
}
