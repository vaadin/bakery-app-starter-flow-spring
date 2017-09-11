package com.vaadin.starter.bakery.ui.components;

import static com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil.convertIfNotEmpty;
import static com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil.convertIfNotNull;

import com.vaadin.annotations.ClientDelegate;
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
	
	@Id("amount")
	private TextField amount;

	private boolean enabledIfNotReadOnly;

	public AmountField() {
		amount.setReadOnly(true);
		getModel().setEnabled(false);
		amount.addValueChangeListener(e -> {
			fireEvent(new ValueChangeEvent<AmountField, Integer>(this, this,
					convertIfNotEmpty(e.getOldValue(), Integer::parseInt), false));
		});
	}

	@Override
	public AmountField setValue(Integer value) {
		amount.setReadOnly(false);
		amount.setValue(convertIfNotNull(value, Object::toString));
		if (value != null && value.intValue() > 0) {
			setEnabled(true);
		}
		getModel().setValue(value);
		amount.setReadOnly(true);
		return this;
	}

	@Override
	public Integer getValue() {
		return convertIfNotEmpty(amount.getValue(), Integer::parseInt);
	}

	public void setEnabled(boolean enabled) {
		enabledIfNotReadOnly = enabled;
		changeEnabled();
	}

	private void changeEnabled() {
		boolean enabled = enabledIfNotReadOnly && !isReadOnly();
		getModel().setEnabled(enabled);
		if (enabled && getValue() == null) {
			setValue(1);
		}
	}

	@EventHandler
	public void plus() {
		change(1);
	}

	@EventHandler
	public void minus() {
		change(-1);
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

	@Override
	public void setReadOnly(boolean readOnly) {
		HasValue.super.setReadOnly(readOnly);
		changeEnabled();
		// amount.setReadOnly(readOnly);
	}

	public interface Model extends TemplateModel {

		void setEnabled(boolean enabled);

		void setValue(Integer value);
	}
}
