package com.vaadin.starter.bakery.backend.data.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserTest {

	@Test
	public void equalsTest() {
		User o1 = new User();
		o1.setPasswordHash("hash");
		o1.setEmail("abc@vaadin.com");
		o1.setFirstName("first");
		o1.setLastName("last");
		o1.setRole("role");

		User o2 = new User();
		o2.setPasswordHash("anotherhash");
		o2.setEmail("abc@vaadin.com");
		o2.setFirstName("anotherName");
		o2.setLastName("last");
		o2.setRole("role");

		Assertions.assertNotEquals(o1, o2);

		o2.setFirstName("first");
		Assertions.assertEquals(o1, o2);
	}
}
