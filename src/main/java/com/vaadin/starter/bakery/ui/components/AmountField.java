package com.vaadin.starter.bakery.ui.components;

import static com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil.convertIfNotEmpty;
import static com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil.convertIfNotNull;

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

	@Id("amount")
	private TextField amount;

	private boolean enabledIfNotReadOnly;

	public AmountField() {
		getModel().setEnabled(false);
		amount.addValueChangeListener(e -> {
			fireEvent(new ValueChangeEvent<AmountField, Integer>(this, this, convertIfNotEmpty(e.getOldValue(), Integer::parseInt), false));
		});
	}

	@Override
	public AmountField setValue(Integer value) {
		amount.setValue(convertIfNotNull(value, Object::toString));
		if(value != null && value.intValue() > 0) {
			setEnabled(true);
		}
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
	
	@Override
	public void setReadOnly(boolean readOnly) {
		HasValue.super.setReadOnly(readOnly);
		changeEnabled();
		// amount.setReadOnly(readOnly);
	}

	
	public interface Model extends TemplateModel {

		void setEnabled(boolean enabled);
	}
}
