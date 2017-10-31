package com.vaadin.starter.bakery.backend.data;

public enum OrderState {
	NEW, CONFIRMED, READY, DELIVERED, PROBLEM, CANCELLED;

	/**
	 * Gets a version of the enum identifier in a human friendly format.
	 *
	 * @return a human friendly version of the identifier
	 */
	//TODO should return
	//SharedUtil.upperCaseUnderscoreToHumanFriendly(name()
	public String getDisplayName() {
		return name().toLowerCase();
	}
}
