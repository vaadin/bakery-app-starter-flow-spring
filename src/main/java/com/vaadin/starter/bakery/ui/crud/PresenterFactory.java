/**
 * 
 */
package com.vaadin.starter.bakery.ui.crud;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.service.OrderService;
import com.vaadin.starter.bakery.backend.service.ProductService;
import com.vaadin.starter.bakery.backend.service.UserService;

@Configuration
public class PresenterFactory {

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public DefaultEntityPresenter<Product> productPresenter(ProductService crudService, User currentUser) {
		return new DefaultEntityPresenter<>(new EntityPresenter<>(crudService, currentUser), crudService);
	}

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public DefaultEntityPresenter<User> userPresenter(UserService crudService, User currentUser) {
		return new DefaultEntityPresenter<>(new EntityPresenter<>(crudService, currentUser), crudService);
	}

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public EntityPresenter<Order> orderEntityPresenter(OrderService crudService, User currentUser) {
		return new EntityPresenter<>(crudService, currentUser);
	}

}