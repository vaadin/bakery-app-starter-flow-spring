package com.vaadin.starter.bakery.ui.view;

import com.vaadin.starter.bakery.By;
import com.vaadin.starter.bakery.elements.GridElement;
import com.vaadin.starter.bakery.ui.components.storefront.OrderEditElement;
import com.vaadin.starter.bakery.ui.components.storefront.StoreFrontItemDetailWrapperElement;
import com.vaadin.starter.bakery.ui.components.storefront.ViewSelectorElement;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elementsbase.Element;
import org.openqa.selenium.WebElement;

@Element("bakery-storefront")
public class StoreFrontViewElement extends TestBenchElement implements HasApp, HasGrid {

	@Override
	public GridElement getGrid() {
		return $(GridElement.class).id("list");
	}

	public StoreFrontItemDetailWrapperElement getOrderDetailWrapper(int index) {
		WebElement element = getGrid().findElements(By.cssSelector("storefront-item-detail-wrapper")).get(index);
		return TestBenchElement.wrapElement(element, getCommandExecutor())
				.wrap(StoreFrontItemDetailWrapperElement.class);
	}

	private ViewSelectorElement getOrderDialog() {
		return findElement(By.tagName("view-selector")).wrap(ViewSelectorElement.class);
	}

	public OrderEditElement getOrderEdit() {
		return getOrderDialog().$(OrderEditElement.class).first();
	}
}
