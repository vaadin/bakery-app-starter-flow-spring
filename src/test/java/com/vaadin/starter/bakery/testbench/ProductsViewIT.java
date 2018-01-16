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
	public void editProduct() throws InterruptedException {
		ProductsViewElement productsPage = openProductsPage();

		Assert.assertFalse(productsPage.getDialog().isOpened());

		String url = getDriver().getCurrentUrl();
		productsPage.getGridCell("Strawberry Bun").click();
		Thread.sleep(100);

		Assert.assertTrue(getDriver().getCurrentUrl().length() > url.length());

		Assert.assertTrue(productsPage.getDialog().isOpened());

		String initialValue = productsPage.getPrice().getValue();

		productsPage.getPrice().setValue("123.45");

		productsPage.getButtonsBar().getSaveButton().click();

		Assert.assertFalse(productsPage.getDialog().isOpened());

		Assert.assertTrue(getDriver().getCurrentUrl().endsWith("products"));

		productsPage.getGridCell("Strawberry Bun").click();
		Assert.assertEquals("123.45", productsPage.getPrice().getValue());

		//Return initial value
		productsPage.getPrice().setValue(initialValue);

		productsPage.getButtonsBar().getSaveButton().click();
	}

}
