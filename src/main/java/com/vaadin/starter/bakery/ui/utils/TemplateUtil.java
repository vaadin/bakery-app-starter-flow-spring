package com.vaadin.starter.bakery.ui.utils;

import com.vaadin.flow.component.Component;

public class TemplateUtil {

	public static void addToSlot(Component parent, Component child, String slotName) {
		setSlot(child, slotName);
		parent.getElement().appendChild(child.getElement());
	}

	public static void setSlot(Component child, String slotName) {
		child.getElement().setAttribute("slot", slotName);
	}

}
