package com.vaadin.starter.bakery.ui.components;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Id;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.flow.event.ComponentEventListener;
import com.vaadin.starter.bakery.ui.BakerySearch;
import com.vaadin.ui.Button;
import com.vaadin.ui.HasClickListeners.ClickEvent;

import java.util.function.Consumer;

@Tag("items-view")
@HtmlImport("frontend://src/elements/items-view.html")
public class ItemsView extends PolymerTemplate<ItemsView.Model> {

	public interface Model extends TemplateModel {
		void setEditing(Boolean editing);
	}

	private BakerySearch searchBar;

	public ItemsView() {
		searchBar = new BakerySearch();
		searchBar.getElement().setAttribute("slot", "item-search");
		searchBar.setPlaceHolder("Search");

		getElement().appendChild(searchBar.getElement());
	}

	public void addFilterChangeListener(Consumer<String> consumer) {
		searchBar.addFilterChangeListener((filter, showPrevious) -> consumer.accept(filter));
	}

	public void addActionClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
		searchBar.addActionClickListener(listener);
	}

	public void setActionText(String actionText) {
		searchBar.setActionText(actionText);
	}

	public String getFilter() {
		return searchBar.getFilter();
	}

	public void openDialog(boolean open) {
		getModel().setEditing(open);
	}
}
