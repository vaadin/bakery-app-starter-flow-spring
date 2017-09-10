package com.vaadin.starter.bakery.ui.components;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.dom.DomEventListener;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.shared.Registration;

@Tag("items-view")
@HtmlImport("frontend://src/elements/items-view.html")
public class ItemsView extends PolymerTemplate<ItemsView.Model> {

	public interface Model extends TemplateModel {
		void setEditing(Boolean editing);
	}

	public void openDialog(boolean open) {
		getModel().setEditing(open);
	}

	public Registration addNewListener(DomEventListener listener) {
		return getElement().addEventListener("new-item", listener);
	}
}
