package com.vaadin.starter.bakery.ui.components;

import com.vaadin.flow.model.TemplateModel;
import com.vaadin.ui.Tag;
import com.vaadin.ui.button.Button;
import com.vaadin.ui.common.HasClickListeners;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.event.ComponentEvent;
import com.vaadin.ui.event.ComponentEventListener;
import com.vaadin.ui.event.DomEvent;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;
import com.vaadin.ui.textfield.TextField;

@Tag("bakery-search")
@HtmlImport("src/app/bakery-search.html")
public class BakerySearch extends PolymerTemplate<BakerySearch.Model> {

	public interface Model extends TemplateModel {
		Boolean getCheckboxChecked();

		void setCheckboxChecked(boolean checkboxChecked);

		void setCheckboxText(String checkboxText);

		void setButtonText(String actionText);
	}

	@Id("field")
	private TextField textField;

	@Id("clear")
	private Button clearButton;

	@Id("action")
	private Button actionButton;

	public BakerySearch() {
		clearButton.addClickListener(e -> {
			if (!textField.isEmpty()) {
				textField.clear();
			}
			getModel().setCheckboxChecked(false);
		});

		getElement().addPropertyChangeListener("checkboxChecked", e -> fireEvent(new FilterChanged(this, false)));
	}

	public String getFilter() {
		return textField.getValue();
	}

	public boolean isCheckboxChecked() {
		return getModel().getCheckboxChecked();
	}

	public void setPlaceHolder(String placeHolder) {
		textField.setPlaceholder(placeHolder);
	}

	public void setActionText(String actionText) {
		getModel().setButtonText(actionText);
	}

	public void setCheckboxText(String checkboxText) {
		getModel().setCheckboxText(checkboxText);
	}

	@DomEvent("filter-changed")
	public static class FilterChanged extends ComponentEvent<BakerySearch> {
		public FilterChanged(BakerySearch source, boolean fromClient) {
			super(source, fromClient);
		}
	}

	public void addFilterChangeListener(ComponentEventListener<FilterChanged> listener) {
		addListener(FilterChanged.class, listener);
	}

	public void addActionClickListener(ComponentEventListener<HasClickListeners.ClickEvent<Button>> listener) {
		actionButton.addClickListener(listener);
	}
}
