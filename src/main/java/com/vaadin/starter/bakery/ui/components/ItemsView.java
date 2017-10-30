package com.vaadin.starter.bakery.ui.components;

import com.vaadin.flow.model.TemplateModel;
import com.vaadin.starter.bakery.ui.BakerySearch;
import com.vaadin.ui.Tag;
import com.vaadin.ui.button.Button;
import com.vaadin.ui.common.HasClickListeners;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.event.ComponentEventListener;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

import java.util.function.Consumer;

@Tag("items-view")
@HtmlImport("src/elements/items-view.html")
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

	public void addActionClickListener(ComponentEventListener<HasClickListeners.ClickEvent<Button>> listener) {
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
