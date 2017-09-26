package com.vaadin.starter.bakery.ui;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Id;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.event.ComponentEventListener;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.ui.Button;
import com.vaadin.ui.HasClickListeners.ClickEvent;
import com.vaadin.ui.TextField;

import java.util.function.BiConsumer;

@Tag("bakery-search")
@HtmlImport("context://src/app/bakery-search.html")
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
	}

	public String getFilter() {
		return textField.getValue();
	}

	public boolean getShowPrevious() {
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

	public void addFilterChangeListener(BiConsumer<String, Boolean> consumer) {
		textField.addValueChangeListener(
				e -> consumer.accept(textField.getValue(), getModel().getCheckboxChecked()));
		getElement().addPropertyChangeListener("checkboxChecked",
				e -> consumer.accept(textField.getValue(), getModel().getCheckboxChecked()));
	}

	public void addActionClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
		actionButton.addClickListener(listener);
	}
}
