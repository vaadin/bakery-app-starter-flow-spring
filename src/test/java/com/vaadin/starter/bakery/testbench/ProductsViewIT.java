package com.vaadin.starter.bakery.testbench;

import java.util.Random;

import com.vaadin.testbench.addons.junit5.extensions.unittest.VaadinTest;
import org.junit.jupiter.api.Assertions;

import com.vaadin.flow.component.grid.testbench.GridElement;
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

	@VaadinTest
	public void editProductTwice() {
		ProductsViewElement productsPage = openView();

		String uniqueName = "Unique cake name " + r.nextInt();
		String initialPrice = "98.76";
		int rowNum = createProduct(productsPage, uniqueName, initialPrice);
		productsPage.openRowForEditing(rowNum);

		Assertions.assertTrue(productsPage.isEditorOpen());
		String newValue = "New " + uniqueName;
		TextFieldElement nameField = productsPage.getProductName();
		nameField.setValue(newValue);

		productsPage.getEditorSaveButton().click();
		Assertions.assertFalse(productsPage.isEditorOpen());
		GridElement grid = productsPage.getGrid();
		Assertions.assertEquals(rowNum, grid.getCell(newValue).getRow());

		productsPage.openRowForEditing(rowNum);
		newValue = "The " + newValue;
		nameField = productsPage.getProductName();
		nameField.setValue(newValue);

		productsPage.getEditorSaveButton().click();
		Assertions.assertFalse(productsPage.isEditorOpen());
		Assertions.assertEquals(rowNum, grid.getCell(newValue).getRow());
	}

	@VaadinTest
	public void editProduct() {
		ProductsViewElement productsPage = openView();

		String url = getDriver().getCurrentUrl();

		String uniqueName = "Unique cake name " + r.nextInt();
		String initialPrice = "98.76";
		int rowIndex = createProduct(productsPage, uniqueName, initialPrice);

		productsPage.openRowForEditing(rowIndex);
		Assertions.assertTrue(getDriver().getCurrentUrl().length() > url.length());

		Assertions.assertTrue(productsPage.isEditorOpen());

		TextFieldElement price = productsPage.getPrice();
		Assertions.assertEquals(initialPrice, price.getValue());

		price.setValue("123.45");

		productsPage.getEditorSaveButton().click();

		Assertions.assertFalse(productsPage.isEditorOpen());

		Assertions.assertTrue(getDriver().getCurrentUrl().endsWith("products"));

		productsPage.openRowForEditing(rowIndex);

		price = productsPage.getPrice(); // Requery the price element.
		Assertions.assertEquals("123.45", price.getValue());

		// Return initial value
		price.setValue(initialPrice);

		productsPage.getEditorSaveButton().click();
		Assertions.assertFalse(productsPage.isEditorOpen());
	}

	@VaadinTest
	public void testCancelConfirmationMessage() {
		ProductsViewElement productsPage = openView();

		productsPage.getNewItemButton().get().click();
		Assertions.assertTrue(productsPage.isEditorOpen());
		productsPage.getProductName().setValue("Some name");
		productsPage.getEditorCancelButton().click();
		Assertions.assertEquals(productsPage.getDiscardConfirmDialog().getHeaderText(), "Discard changes");
	}

	private int createProduct(ProductsViewElement productsPage, String name, String price) {
		productsPage.getSearchBar().getCreateNewButton().click();

		Assertions.assertTrue(productsPage.isEditorOpen());

		TextFieldElement nameField = productsPage.getProductName();
		TextFieldElement priceField = productsPage.getPrice();

		nameField.setValue(name);
		priceField.setValue(price);

		productsPage.getEditorSaveButton().click();
		Assertions.assertFalse(productsPage.isEditorOpen());

		return pageObject.waitUntil(wd -> productsPage.getGrid().getCell(name)).getRow();
	}
}
