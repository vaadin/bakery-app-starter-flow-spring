package com.vaadin.starter.bakery.testbench.elements.components;

import com.vaadin.flow.component.html.testbench.DivElement;
import com.vaadin.flow.component.html.testbench.SpanElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("order-card")
public class OrderCardElement extends DivElement {

	public String getGoodsCount(int index) {
		SpanElement count = $(DivElement.class).withAttributeContainingWord("class", "goods-item").get(index)
				.$(SpanElement.class).first();
		return count.getText();
	}

	@Override
	public void click() {
		$(DivElement.class).withAttributeContainingWord("class", "wrapper").first().click();
	}
}
