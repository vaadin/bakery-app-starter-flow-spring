package com.vaadin.starter.bakery.ui.view;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.vaadin.starter.bakery.AbstractIT;
import com.vaadin.starter.bakery.elements.ButtonElement;
import com.vaadin.starter.bakery.elements.GridElement;
import com.vaadin.starter.bakery.ui.components.storefront.StorefrontOrderCardElement;

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
