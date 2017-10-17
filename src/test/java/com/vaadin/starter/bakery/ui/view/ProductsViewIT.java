package com.vaadin.starter.bakery.ui.view;

import com.vaadin.starter.bakery.AbstractIT;
import com.vaadin.starter.bakery.elements.GridElement;
import com.vaadin.starter.bakery.ui.components.ItemDetailDialogElement;
import com.vaadin.starter.bakery.ui.components.ProductEditElement;
import com.vaadin.starter.bakery.ui.components.storefront.StoreFrontItemDetailWrapperElement;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import java.util.concurrent.TimeUnit;

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
		getDriver().getCurrentUrl().endsWith("products/1");
		Assert.assertTrue(editor.isDisplayed());

		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		ProductEditElement editElement = productsPage.getProductEdit();
		editElement.getPrice().setValue("123.12");

		editElement.getEditForm().getSaveButton();

		Assert.assertFalse(editor.isDisplayed());
		getDriver().getCurrentUrl().endsWith("products/");

		productsPage.getGridCell("Strawberry Bun").click();
		Assert.assertTrue(productsPage.getProductEdit().getPrice().getValue().equals("123.12"));

		try {
			TimeUnit.SECONDS.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
