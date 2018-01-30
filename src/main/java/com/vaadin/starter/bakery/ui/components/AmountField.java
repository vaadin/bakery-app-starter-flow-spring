package com.vaadin.starter.bakery.ui.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;

@Tag("amount-field")
@HtmlImport("src/components/amount-field.html")
public class AmountField extends Component implements HasValue<AmountField, Integer> {

	private static final int MIN = 1;
	private static final int MAX = 15;

	public AmountField() {
		setMin(MIN);
		setMax(MAX);
	}

	@Override
	public void setValue(Integer value) {
		this.getElement().setProperty("value", value);
	}

	@Override
	public Integer getValue() {
		String val = this.getElement().getProperty("value");
		return val == null ? 0 : Integer.parseInt(val);
	}

	public void setDisabled(boolean disabled) {
		this.getElement().setProperty("disabled", disabled);
	}

	public void setMin(int value) {
		this.getElement().setProperty("min", value);
	}

	public void setMax(int value) {
		this.getElement().setProperty("max", value);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		this.getElement().setProperty("readOnly", readOnly);
	}
}
