package com.vaadin.starter.bakery.test;

import java.util.Locale;
import java.util.Locale.Category;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class FormattingTest {

	private static Locale locale;

	@BeforeClass
	public static void setUpClass() {
		locale = Locale.getDefault(Category.FORMAT);
		Locale.setDefault(Category.FORMAT, new Locale("tr", "TR"));
	}

	@AfterClass
	public static void tearDownClass() {
		Locale.setDefault(Category.FORMAT, locale);
		locale = null;
	}

}