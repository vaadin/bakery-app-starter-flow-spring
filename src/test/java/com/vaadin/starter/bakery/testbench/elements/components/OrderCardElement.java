package com.vaadin.starter.bakery.testbench.elements.components;

import com.vaadin.flow.component.html.testbench.DivElement;
import com.vaadin.flow.component.html.testbench.SpanElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("order-card")
public class OrderCardElement extends TestBenchElement {

	public String getGoodsCount(int index) {
		SpanElement count = $(DivElement.class).id("goods").$(DivElement.class).get(index).$(SpanElement.class).first();
		return count.getText();
	}
}
