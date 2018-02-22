package com.vaadin.starter.bakery.ui.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Synchronize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;

@Tag("amount-field")
@HtmlImport("src/components/amount-field.html")
public class AmountField extends Component implements HasValue<AmountField, Integer> {

	@Override
	public void setValue(Integer value) {
		getElement().setProperty("value", value);
	}

	@Override
	@Synchronize("value-changed")
	public Integer getValue() {
		String val = getElement().getProperty("value");
		try {
			return Integer.parseInt(val);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public void setEnabled(boolean enabled) {
		getElement().setProperty("disabled", !enabled);
	}

	public void setMin(int value) {
		getElement().setProperty("min", value);
	}

	public void setMax(int value) {
		getElement().setProperty("max", value);
	}

	public void setEditable(boolean editable) {
		getElement().setProperty("editable", editable);
	}

	public void setPattern(String pattern) {
		getElement().setProperty("pattern", pattern);
	}

}
