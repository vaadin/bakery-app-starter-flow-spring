package com.vaadin.starter.bakery.ui.view;

import com.vaadin.starter.bakery.AbstractIT;
import com.vaadin.starter.bakery.ui.components.ItemDetailDialogElement;
import com.vaadin.starter.bakery.ui.components.ProductEditElement;
import org.junit.Assert;
import org.junit.Test;

public class ProductsViewIT extends AbstractIT {

	private ProductsViewElement openProductsPage() {
		StoreFrontViewElement storefront = openLoginView().login("admin@vaadin.com", "admin");
		return storefront.getMenu().navigateToProducts();
	}

	@Test
	public void editProduct() {
		ProductsViewElement productsPage = openProductsPage();

		ItemDetailDialogElement editor = productsPage.getItemsView().getEditorDialog();
		Assert.assertFalse(editor.isDisplayed());

		productsPage.getGridCell("Strawberry Bun").click();
		Assert.assertTrue(getDriver().getCurrentUrl().endsWith("products/1"));
		Assert.assertTrue(editor.isDisplayed());

		ProductEditElement editElement = productsPage.getProductEdit();
		Assert.assertNotNull(editElement);
		String initialValue = editElement.getPrice().getValue();

		editElement.getPrice().setValue("123.45");

		editElement.getSaveButton().click();

		Assert.assertFalse(editor.isDisplayed());
		Assert.assertTrue(getDriver().getCurrentUrl().endsWith("products/"));

		productsPage.getGridCell("Strawberry Bun").click();
		Assert.assertTrue(productsPage.getProductEdit().getPrice().getValue().equals("123.45"));

		//Return initial value
		editElement.getPrice().setValue(initialValue);

		editElement.getSaveButton().click();

	}

}
