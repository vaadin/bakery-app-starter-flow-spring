package com.vaadin.starter.bakery.ui.utils;

import com.vaadin.flow.router.LocationChangeEvent;
import com.vaadin.starter.bakery.ui.presenter.EntityEditPresenter;
import com.vaadin.ui.Component;

public class TemplateUtil {

	public static void addToSlot(Component parent, Component child, String slotName) {
		setSlot(child, slotName);
		parent.getElement().appendChild(child.getElement());
	}

	public static void setSlot(Component child, String slotName) {
		child.getElement().setAttribute("slot", slotName);
	}

	public static void handleEntityNavigation(EntityEditPresenter<?> presenter, LocationChangeEvent locationChangeEvent,
			boolean edit) {
		try {
			String id = locationChangeEvent.getPathParameter("id");
			boolean idProvided = id != null && !id.isEmpty();
			if (idProvided) {
				presenter.loadEntity(Long.parseLong(id), edit);
			}
		} catch (NumberFormatException e) {
			// Bad id
			locationChangeEvent.rerouteToErrorView();
		}
	}
}
