/**
 * 
 */
package com.vaadin.starter.bakery.ui.entities.name;

public final class NameUtil {

	public static String getEntityName(Class<?> clazz) {
		// All needed entities have simple one word names, so this is sufficient. An
		// annotation could be used to override the entity names when required.
		return clazz.getSimpleName();
	}
}
