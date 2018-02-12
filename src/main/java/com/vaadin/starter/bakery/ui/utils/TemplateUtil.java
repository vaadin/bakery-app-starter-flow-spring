package com.vaadin.starter.bakery.ui.utils;

import com.vaadin.flow.component.Component;

public class TemplateUtil {

	public static void addToSlot(Component parent, String slotName, Component child) {
		setSlot(child, slotName);
		parent.getElement().appendChild(child.getElement());
	}

	public static void setSlot(Component child, String slotName) {
		child.getElement().setAttribute("slot", slotName);
	}

	public static String generateLocation(String basePage, String entityId) {
		return basePage + (entityId == null || entityId.isEmpty() ? "" : "/" + entityId);
	}
}
