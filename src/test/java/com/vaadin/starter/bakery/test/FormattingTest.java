package com.vaadin.starter.bakery.test;

import java.util.Locale;
import java.util.Locale.Category;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

/**
 * Test superclass that runs tests in Turkish locale.
 */
public abstract class FormattingTest {

	private static Locale locale;

	@BeforeAll
	public static void setUpClass() {
		locale = Locale.getDefault(Category.FORMAT);
		Locale.setDefault(Category.FORMAT, new Locale("tr", "TR"));
	}

	@AfterAll
	public static void tearDownClass() {
		Locale.setDefault(Category.FORMAT, locale);
		locale = null;
	}

}