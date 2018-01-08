/**
 * 
 */
package com.vaadin.starter.bakery.ui.view;


public interface CrudOperationListener<T> {

	void execute(T entity);
}
