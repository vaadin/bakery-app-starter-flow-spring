package com.vaadin.starter.bakery.testbench;

import org.junit.Assert;
import org.junit.Test;

import com.vaadin.starter.bakery.testbench.elements.ui.ProductsViewElement;
import com.vaadin.starter.bakery.testbench.elements.ui.StorefrontViewElement;

public class ProductsViewIT extends AbstractIT {

	private ProductsViewElement openProductsPage() {
		StorefrontViewElement storefront = openLoginView().login("admin@vaadin.com", "admin");
		return storefront.getMenu().navigateToProducts();
	}

	@Test
	public void editProduct() {
		ProductsViewElement productsPage = openProductsPage();

		Assert.assertFalse(productsPage.getDialog().isOpen());

		String url = getDriver().getCurrentUrl();
		productsPage.getGrid().getCell("Strawberry Bun").click();
		Assert.assertTrue(getDriver().getCurrentUrl().length() > url.length());

		Assert.assertTrue(productsPage.getDialog().isOpen());

		String initialValue = productsPage.getPrice().getValue();

		productsPage.getPrice().setValue("123.45");

		productsPage.getButtonsBar().getSaveButton().click();

		Assert.assertFalse(productsPage.getDialog().isOpen());

		Assert.assertTrue(getDriver().getCurrentUrl().endsWith("products"));

		productsPage.getGrid().getCell("Strawberry Bun").click();
		Assert.assertEquals("123.45", productsPage.getPrice().getValue());

		// Return initial value
		productsPage.getPrice().setValue(initialValue);

		productsPage.getButtonsBar().getSaveButton().click();
	}

}
