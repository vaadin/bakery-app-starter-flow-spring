package com.vaadin.starter.bakery.ui.view;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;

import com.vaadin.starter.bakery.AbstractIT;

public class LoginIT extends AbstractIT {

    @Test
    public void logingWorks() {
        LoginViewElement loginView = openLoginView();
        Assert.assertEquals("Email", findElement(By.id("login-label")).getText());
        loginView.login("barista@vaadin.com", "barista");
        Assert.assertEquals(0, findElements(By.id("login-label")).size());
    }

}