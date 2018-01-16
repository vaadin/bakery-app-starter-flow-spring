package com.vaadin.starter.bakery.testbench;

import org.junit.Assert;
import org.junit.Test;

import com.vaadin.starter.bakery.testbench.elements.components.StorefrontOrderCardElement;
import com.vaadin.starter.bakery.testbench.elements.core.ButtonElement;
import com.vaadin.starter.bakery.testbench.elements.core.GridElement;
import com.vaadin.starter.bakery.testbench.elements.ui.StorefrontViewElement;

public class StorefrontViewIT extends AbstractIT {

	private StorefrontViewElement openStorefrontPage() {
		return openLoginView().login("admin@vaadin.com", "admin");
	}

	@Test
	public void orderCardExpandAndCollapse() throws InterruptedException {
		StorefrontViewElement storefrontPage = openStorefrontPage();

		GridElement grid = storefrontPage.getGrid();
		Assert.assertTrue(grid.getGridSize() >= 0);

		StorefrontOrderCardElement firstOrder = storefrontPage.getFirstOrderCard();
		Assert.assertNotNull(firstOrder);
		Assert.assertFalse(firstOrder.isOrderSelected());

		firstOrder.click();
		Thread.sleep(100);
		Assert.assertTrue(firstOrder.isOrderSelected());
	}

	@Test
	public void editOrder() throws InterruptedException {
		StorefrontViewElement storefrontPage = openStorefrontPage();

		StorefrontOrderCardElement firstOrder = storefrontPage.getFirstOrderCard();
		Assert.assertNotNull(firstOrder);
		firstOrder.click();
		Thread.sleep(100);

		ButtonElement editBtn = firstOrder.getDetail().getEditButton();
		editBtn.scrollIntoView();
		editBtn.click();

		// FIXME: regression in vaadin-alpha13
		// For some reason TB getAttribute("selected") throws an exception
		// complaining about the element is not attached to the page.
		// Assert.assertFalse(firstOrder.isOrderSelected());

		Thread.sleep(200);
		Assert.assertTrue(getDriver().getCurrentUrl().endsWith("edit="));
	}

}
