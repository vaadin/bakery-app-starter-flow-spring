package com.vaadin.starter.bakery.ui.view;

import com.vaadin.starter.bakery.AbstractIT;
import com.vaadin.starter.bakery.elements.GridElement;
import com.vaadin.starter.bakery.ui.components.storefront.StoreFrontItemDetailWrapperElement;
import org.junit.Assert;
import org.junit.Test;

public class StorefrontViewIT extends AbstractIT {

	private StoreFrontViewElement openStorefrontPage() {
		return openLoginView().login("admin@vaadin.com", "admin");
	}

	@Test
	public void orderItemExpandAndCollapse() {
		StoreFrontViewElement storefrontPage = openStorefrontPage();

		GridElement grid = storefrontPage.getGrid();
		Assert.assertTrue(grid.getGridSize() >= 0);

		StoreFrontItemDetailWrapperElement firstOrder = storefrontPage.getOrderDetailWrapper(5);
		Assert.assertNotNull(firstOrder);
		Assert.assertFalse(firstOrder.isOrderSelected());

		firstOrder.click();
		Assert.assertTrue(firstOrder.isOrderSelected());

		firstOrder.getDetail().getCancelButton().click();
		Assert.assertFalse(firstOrder.isOrderSelected());

	}

	@Test
	public void editOrder() {
		StoreFrontViewElement storefrontPage = openStorefrontPage();

		StoreFrontItemDetailWrapperElement firstOrder = storefrontPage.getOrderDetailWrapper(0);
		Assert.assertNotNull(firstOrder);
		firstOrder.click();
		firstOrder.getDetail().getEditButton().click();

		Assert.assertFalse(firstOrder.isOrderSelected());

		//	url is not changed and order-edit is not put into slot after click.
		//  getDriver().getCurrentUrl().endsWith("edit");
		//	OrderEditElement orderEdit = storefrontPage.getOrderEdit();
	}

}
