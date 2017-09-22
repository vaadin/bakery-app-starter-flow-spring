package com.vaadin.starter.bakery.ui.utils;

import com.vaadin.ui.polymertemplate.PolymerTemplate;

public class TemplateUtil {

	public static void addToSlot(PolymerTemplate<?> parent, PolymerTemplate<?> child, String slotName) {
		child.getElement().setAttribute("slot", slotName);
		parent.getElement().appendChild(child.getElement());
	}
}
