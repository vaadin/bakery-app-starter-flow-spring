package com.vaadin.starter.bakery.ui.components.viewselector;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Tag;
import com.vaadin.external.jsoup.helper.Validate;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;

@Tag("view-selector")
@HtmlImport("context://src/elements/viewselector/view-selector.html")
public class ViewSelector extends PolymerTemplate<TemplateModel> implements HasComponents {

	public void select(Component component) {
		Validate.isTrue(this.equals(component.getParent().get()));
		getElement().getChildren().forEach(child -> {
			boolean visible = child.getComponent().isPresent() && child.getComponent().get().equals(component);
			child.setAttribute("hidden", !visible);
		});
	}

}
