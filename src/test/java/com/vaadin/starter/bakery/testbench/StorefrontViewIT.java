package com.vaadin.starter.bakery.testbench;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.starter.bakery.testbench.elements.components.OrderCardElement;
import com.vaadin.starter.bakery.testbench.elements.ui.StorefrontViewElement;

import static org.hamcrest.CoreMatchers.endsWith;

public class StorefrontViewIT extends AbstractIT {

	private StorefrontViewElement openStorefrontPage() {
		return openLoginView().login("admin@vaadin.com", "admin");
	}

	@Test
	@Ignore("BFF-614")
	public void editOrder() {
		StorefrontViewElement storefrontPage = openStorefrontPage();

		OrderCardElement firstOrder = storefrontPage.getOrderCard(0);
		Assert.assertNotNull(firstOrder);
		firstOrder.click();

		ButtonElement editBtn = storefrontPage.getOrderDetails().getEditButton();
		editBtn.click();

		Assert.assertThat(getDriver().getCurrentUrl(), endsWith("edit="));
	}

}
