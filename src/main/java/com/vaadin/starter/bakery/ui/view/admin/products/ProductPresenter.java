/**
 * 
 */
package com.vaadin.starter.bakery.ui.view.admin.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.service.FilterableCrudService;
import com.vaadin.starter.bakery.ui.crud.DefaultEntityPresenter;

@SpringComponent
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class ProductPresenter extends DefaultEntityPresenter<Product> {

	@Autowired
	ProductPresenter(FilterableCrudService<Product> crudService, User currentUser) {
		super(crudService, "Product", currentUser);
	}
}
