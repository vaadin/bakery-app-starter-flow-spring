package com.vaadin.starter.bakery.testbench;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.Keys;

import com.vaadin.flow.component.grid.testbench.GridElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
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
		GridElement grid = productsPage.getGrid();
		grid.getCell("Strawberry Bun").click();
		Assert.assertTrue(getDriver().getCurrentUrl().length() > url.length());

		Assert.assertTrue(productsPage.getDialog().isOpen());

		TextFieldElement price = productsPage.getPrice();
		String initialValue = price.getValue();

		price.focus();
		price.setValue("123.45");
		price.sendKeys(Keys.TAB);
		productsPage.getButtonsBar().getSaveButton().click();

		Assert.assertFalse(productsPage.getDialog().isOpen());

		Assert.assertTrue(getDriver().getCurrentUrl().endsWith("products"));

		grid.getCell("Strawberry Bun").click();
		Assert.assertEquals("123.45", price.getValue());

		// Return initial value
		price.focus();
		price.setValue(initialValue);
		price.sendKeys(Keys.TAB);
		productsPage.getButtonsBar().getSaveButton().click();
	}

}
