package com.vaadin.starter.bakery.testbench;

import org.junit.Assert;
import org.junit.Test;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.starter.bakery.testbench.elements.components.OrderCardElement;
import com.vaadin.starter.bakery.testbench.elements.ui.StorefrontViewElement;
import com.vaadin.starter.bakery.testbench.elements.ui.StorefrontViewElement.OrderEditorElement;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;

public class StorefrontViewIT extends AbstractIT {

	private StorefrontViewElement openStorefrontPage() {
		return openLoginView().login("admin@vaadin.com", "admin");
	}

	@Test
	public void editOrder() {
		StorefrontViewElement storefrontPage = openStorefrontPage();

		OrderCardElement firstOrder = storefrontPage.getOrderCard(0);
		Assert.assertNotNull(firstOrder);
		int initialCount = Integer.parseInt(firstOrder.getGoodsCount(0));

		firstOrder.click();
		ButtonElement editBtn = storefrontPage.getOrderDetails().getEditButton();
		editBtn.getWrappedElement().click();
		Assert.assertTrue(getDriver().getCurrentUrl().contains(BakeryConst.PAGE_STOREFRONT_EDIT));

		OrderEditorElement orderEditor = storefrontPage.getOrderEditor();
		orderEditor.getOrderItemEditor(0).clickAmountFieldPlus(0);
		orderEditor.review();
		storefrontPage.getOrderDetails().getSaveButton().click();

		int currentCount = Integer.parseInt(storefrontPage.getOrderCard(0).getGoodsCount(0));
		Assert.assertEquals(initialCount + 1, currentCount);

	}

}
