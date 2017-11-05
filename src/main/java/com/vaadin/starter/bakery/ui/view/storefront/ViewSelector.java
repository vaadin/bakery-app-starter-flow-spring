package com.vaadin.starter.bakery.ui.view.storefront;

import com.vaadin.flow.model.TemplateModel;
import com.vaadin.ui.Component;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HasComponents;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

@Tag("view-selector")
@HtmlImport("src/elements/viewselector/view-selector.html")
public class ViewSelector extends PolymerTemplate<TemplateModel> implements HasComponents {

	public void select(Component component) {
		if (!this.equals(component.getParent().get())) {
			throw new IllegalArgumentException("Component is not a child of this ViewSelector");
		}
		getElement().getChildren().forEach(child -> {
			boolean visible = child.getComponent().isPresent() && child.getComponent().get().equals(component);
			child.setAttribute("hidden", !visible);
		});
	}

}
