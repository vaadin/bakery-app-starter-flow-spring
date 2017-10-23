package com.vaadin.starter.bakery.ui.view;

import com.vaadin.starter.bakery.AbstractIT;
import com.vaadin.starter.bakery.elements.ButtonElement;
import com.vaadin.starter.bakery.elements.GridElement;
import com.vaadin.starter.bakery.ui.components.storefront.OrderEditElement;
import com.vaadin.starter.bakery.ui.components.storefront.StoreFrontItemDetailWrapperElement;
import org.junit.Assert;
import org.junit.Ignore;
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

		StoreFrontItemDetailWrapperElement firstOrder = storefrontPage.getFirstOrderDetailWrapper();
		Assert.assertNotNull(firstOrder);
		Assert.assertFalse(firstOrder.isOrderSelected());

		firstOrder.click();
		Assert.assertTrue(firstOrder.isOrderSelected());

// 		Cancel button doesn't work sometimes; uncomment after BFF-339 fixed
//		ButtonElement collapseOrderDetails = firstOrder.getDetail().getCancelButton();
//		collapseOrderDetails.scrollIntoView();
//		collapseOrderDetails.click();
//		Assert.assertFalse(firstOrder.isOrderSelected());

	}

	@Ignore("until the issue BFF-339 is fixed")
	@Test
	public void editOrder() {
		StoreFrontViewElement storefrontPage = openStorefrontPage();

		StoreFrontItemDetailWrapperElement firstOrder = storefrontPage.getFirstOrderDetailWrapper();
		Assert.assertNotNull(firstOrder);
		firstOrder.click();
		ButtonElement editBtn = firstOrder.getDetail().getEditButton();
		editBtn.scrollIntoView();
		editBtn.click();

		Assert.assertFalse(firstOrder.isOrderSelected());

		Assert.assertTrue(getDriver().getCurrentUrl().endsWith("edit"));
		OrderEditElement orderEdit = storefrontPage.getOrderEdit();
	}

}
