package com.vaadin.starter.bakery.testbench.elements.ui;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.starter.bakery.testbench.elements.components.OrderCardElement;
import com.vaadin.starter.bakery.testbench.elements.components.OrderDetailsFullElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;

@Element("storefront-view")
public class StorefrontViewElement extends TestBenchElement implements HasApp, HasCrudView {

	@Element("order-editor")
	public static class OrderEditorElement extends TestBenchElement {
		public OrderItemEditorElement getOrderItemEditor(int index) {
			return $(OrderItemEditorElement.class).get(index);
		}

		public void review() {
			$(ButtonElement.class).id("review").click();
		}
	}

	@Override
	public GridElement getGrid() {
		return $(GridElement.class).waitForFirst();
	}

	public OrderCardElement getFirstOrderCard() {
		return getOrderCard(0);
	}

	public OrderCardElement getOrderCard(int index) {
		return getGrid().$(OrderCardElement.class).get(index);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<OrderEditorElement> getFormClass() {
		return OrderEditorElement.class;
	}

	public OrderEditorElement getOrderEditor() {
		return getDialog().$(OrderEditorElement.class).first();
	}

	public OrderDetailsFullElement getOrderDetails() {
		return getDialog().$(OrderDetailsFullElement.class).first();
	}
}
