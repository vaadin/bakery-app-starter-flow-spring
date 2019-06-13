package com.vaadin.starter.bakery.backend.data.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProductTest {

	@Test
	public void equalsTest() {
		Product o1 = new Product();
		o1.setName("name");
		o1.setPrice(123);

		Product o2 = new Product();
		o2.setName("anothername");
		o2.setPrice(123);

		Assertions.assertNotEquals(o1, o2);

		o2.setName("name");
		Assertions.assertEquals(o1, o2);
	}
}
