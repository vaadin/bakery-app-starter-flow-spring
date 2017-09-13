package com.vaadin.starter.bakery.ui.components;

import static com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil.convertIfNotEmpty;
import static com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil.convertIfNotNull;

import com.vaadin.annotations.EventHandler;
import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Id;
import com.vaadin.annotations.Tag;
import com.vaadin.components.data.HasValue;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.ui.TextField;

/**
 * 
 * @author tulio
 *
 */
@Tag("amount-field")
@HtmlImport("frontend://src/elements/amount-field.html")
public class AmountField extends PolymerTemplate<AmountField.Model> implements HasValue<AmountField, Integer> {

	private final int MIN = 1;

	private final int MAX = 15;

	private Integer value;

	private boolean disabled = true;

	public AmountField() {
		getModel().setDisabled(false);
		getModel().setReadOnly(false);
		updateCommands();
	}

	@Override
	public void setValue(Integer value) {
		this.value = value;
		getModel().setValue(value);
		updateCommands();
	}

	@Override
	public Integer getValue() {
		return this.value;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
		getModel().setDisabled(disabled);
		updateCommands();
	}

	@EventHandler
	public void plus() {
		change(1);
	}

	@EventHandler
	public void minus() {
		change(-1);
	}

	private boolean isChangeable() {
		return !isReadOnly() && !disabled;
	}

	private void change(int valueToSum) {
		if (!isReadOnly()) {
			Integer value = getValue();
			if (value != null) {
				value += valueToSum;
				if (value >= MIN && value <= MAX) {
					setValue(value);
				}
			}
		}
	}

	private void updateCommands() {
		Integer value = getValue();
		boolean canAdd = value == null || value < MAX;
		boolean canSubtract = value != null && value > MIN;
		getModel().setPlusEnabled(isChangeable() && canAdd);
		getModel().setMinusEnabled(isChangeable() && canSubtract);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		HasValue.super.setReadOnly(readOnly);
		getModel().setReadOnly(readOnly);
		updateCommands();
	}

	public interface Model extends TemplateModel {

		void setDisabled(boolean disabled);

		void setValue(Integer value);

		void setPlusEnabled(boolean enabled);

		void setMinusEnabled(boolean enabled);

		void setReadOnly(boolean readOnly);
	}
}
