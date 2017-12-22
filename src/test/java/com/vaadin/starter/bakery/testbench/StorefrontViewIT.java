package com.vaadin.starter.bakery.testbench;

import org.junit.Assert;
import org.junit.Ignore;
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
	public void orderCardExpandAndCollapse() {
		StorefrontViewElement storefrontPage = openStorefrontPage();

		GridElement grid = storefrontPage.getGrid();
		Assert.assertTrue(grid.getGridSize() >= 0);

		StorefrontOrderCardElement firstOrder = storefrontPage.getFirstOrderCard();
		Assert.assertNotNull(firstOrder);
		Assert.assertFalse(firstOrder.isOrderSelected());

		firstOrder.click();
		Assert.assertTrue(firstOrder.isOrderSelected());
	}

	@Ignore("until the issue BFF-339 is fixed")
	@Test
	public void editOrder() {
		StorefrontViewElement storefrontPage = openStorefrontPage();

		StorefrontOrderCardElement firstOrder = storefrontPage.getFirstOrderCard();
		Assert.assertNotNull(firstOrder);
		firstOrder.click();
		ButtonElement editBtn = firstOrder.getDetail().getEditButton();
		editBtn.scrollIntoView();
		editBtn.click();

		Assert.assertFalse(firstOrder.isOrderSelected());

		Assert.assertTrue(getDriver().getCurrentUrl().endsWith("edit"));
	}

}
