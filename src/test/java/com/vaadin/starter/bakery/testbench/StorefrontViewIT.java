package com.vaadin.starter.bakery.testbench;

import static org.hamcrest.CoreMatchers.containsString;

import com.vaadin.starter.bakery.testbench.elements.ui.UsersViewElement;
import org.junit.Assert;
import org.junit.Test;

import com.vaadin.flow.component.button.testbench.ButtonElement;
import com.vaadin.flow.component.notification.testbench.NotificationElement;
import com.vaadin.starter.bakery.testbench.elements.components.OrderCardElement;
import com.vaadin.starter.bakery.testbench.elements.ui.StorefrontViewElement;
import com.vaadin.starter.bakery.testbench.elements.ui.StorefrontViewElement.OrderEditorElement;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;

import java.util.Random;

public class StorefrontViewIT extends AbstractIT {

	private StorefrontViewElement openStorefrontPage() {
		return openLoginView().login("admin@vaadin.com", "admin");
	}

	@Test
	public void editOrder() {
		StorefrontViewElement storefrontPage = openStorefrontPage();

		int orderIndex = new Random().nextInt(10);

		OrderCardElement order = storefrontPage.getOrderCard(orderIndex);
		Assert.assertNotNull(order);
		int initialCount = Integer.parseInt(order.getGoodsCount(0));

		order.click();
		ButtonElement editBtn = storefrontPage.getOrderDetails().getEditButton();
		editBtn.getWrappedElement().click();
		Assert.assertThat(getDriver().getCurrentUrl(), containsString(BakeryConst.PAGE_STOREFRONT_EDIT));

		OrderEditorElement orderEditor = storefrontPage.getOrderEditor();
		orderEditor.getOrderItemEditor(0).clickAmountFieldPlus();

		orderEditor.review();
		storefrontPage.getOrderDetails().getSaveButton().click();

		NotificationElement notification = $(NotificationElement.class).last();
		Assert.assertThat(notification.getText(), containsString("was updated"));

		order = storefrontPage.getOrderCard(orderIndex);
		Assert.assertNotNull(order);
		int currentCount = Integer.parseInt(order.getGoodsCount(0));
		Assert.assertEquals(initialCount + 1, currentCount);

	}

	@Test
	public void testDialogs() {
		StorefrontViewElement storefrontPage = openStorefrontPage();
		openAllDialogs(storefrontPage);

		UsersViewElement usersPage = storefrontPage.getMenu().navigateToUsers();
		storefrontPage = usersPage.getMenu().navigateToStorefront();

		openAllDialogs(storefrontPage);
	}

	private void openAllDialogs(StorefrontViewElement storefrontPage) {
		storefrontPage.getSearchBar().getCreateNewButton().click();
		Assert.assertTrue(storefrontPage.getDialog().get().isOpen());
		storefrontPage.getOrderEditor().cancel();
		Assert.assertFalse(storefrontPage.getDialog().get().isOpen());

		storefrontPage.getSearchBar().getCreateNewButton().click();
		Assert.assertTrue(storefrontPage.getDialog().get().isOpen());

		storefrontPage.getOrderEditor().getCustomerNameField().setValue("New customer");
		// outside click should try to close the dialog
		storefrontPage.getMenu().click();
		Assert.assertTrue(storefrontPage.getConfirmDialog().get().isOpen());
		storefrontPage.getConfirmDialog().get().confirm();

		Assert.assertFalse(storefrontPage.getConfirmDialog().get().isOpen());
		Assert.assertFalse(storefrontPage.getDialog().get().isOpen());

		OrderCardElement order = storefrontPage.getOrderCard(0);
		Assert.assertNotNull(order);
		order.click();

		Assert.assertNotNull(storefrontPage.getOrderDetails());

		storefrontPage.getMenu().click();
		Assert.assertFalse(storefrontPage.getDialog().get().isOpen());
	}

}
