package com.vaadin.starter.bakery.testbench;

import static org.hamcrest.CoreMatchers.containsString;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.support.ui.ExpectedCondition;

import com.vaadin.flow.component.grid.testbench.GridTHTDElement;
import com.vaadin.flow.component.textfield.testbench.TextFieldElement;
import com.vaadin.starter.bakery.testbench.elements.ui.ProductsViewElement;
import com.vaadin.starter.bakery.testbench.elements.ui.StorefrontViewElement;

public class ProductsViewIT extends AbstractIT<ProductsViewElement> {

	private static Random r = new Random();

	@Override
	protected ProductsViewElement openView() {
		StorefrontViewElement storefront = openLoginView().login("admin@vaadin.com", "admin");
		return storefront.getMenu().navigateToProducts();
	}

	@Test
	public void editProduct() {
		ProductsViewElement productsPage = openView();

		String url = getDriver().getCurrentUrl();

		String uniqueName = "Unique cake name " + r.nextInt();
		String initialPrice = "98.76";
		int rowIndex = createProduct(productsPage, uniqueName, initialPrice);

		productsPage.openRowForEditing(rowIndex);
		Assert.assertTrue(getDriver().getCurrentUrl().length() > url.length());

		Assert.assertTrue(productsPage.isEditorOpen());

		TextFieldElement price = productsPage.getPrice();
		Assert.assertEquals(initialPrice, price.getValue());

		price.focus();
		price.setValue("123.45");

		productsPage.getEditorSaveButton().click();

		Assert.assertFalse(productsPage.isEditorOpen());

		Assert.assertTrue(getDriver().getCurrentUrl().endsWith("products"));

		productsPage.openRowForEditing(rowIndex);

		price = productsPage.getPrice(); // Requery the price element.
		Assert.assertEquals("123.45", price.getValue());

		// Return initial value
		price.focus();
		price.setValue(initialPrice);

		productsPage.getEditorSaveButton().click();
		Assert.assertFalse(productsPage.isEditorOpen());
	}

	@Test
	public void testCancelConfirmationMessage() {
		ProductsViewElement productsPage = openView();

		productsPage.getNewItemButton().get().click();
		Assert.assertTrue(productsPage.isEditorOpen());
		productsPage.getProductName().setValue("Some name");
		productsPage.getProductName().focus();
		// We need to call sendKeys in order to fire value change event
		// https://github.com/vaadin/vaadin-crud-flow/issues/78
		productsPage.getProductName().sendKeys("a");
		productsPage.getEditorCancelButton().click();
		Assert.assertThat(productsPage.getDiscardConfirmDialog().getMessageText(), containsString("Discard changes"));
	}

	private int createProduct(ProductsViewElement productsPage, String name, String price) {
		productsPage.getSearchBar().getCreateNewButton().click();

		Assert.assertTrue(productsPage.isEditorOpen());

		TextFieldElement nameField = productsPage.getProductName();
		TextFieldElement priceField = productsPage.getPrice();

		nameField.focus();
		nameField.setValue(name);

		priceField.focus();
		priceField.setValue(price);

		productsPage.getEditorSaveButton().click();
		Assert.assertFalse(productsPage.isEditorOpen());

		return waitUntil((ExpectedCondition<GridTHTDElement>) wd -> productsPage.getGrid().getCell(name)).getRow();
	}

}
