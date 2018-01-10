/**
 * 
 */
package com.vaadin.starter.bakery.ui.view.presenter;

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
import com.vaadin.starter.bakery.ui.view.EntityPresenter;
import com.vaadin.starter.bakery.ui.view.JPAPresenter;

@Configuration
public class PresenterFactory {

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public EntityPresenter<Product> productEntityPresenter(ProductService crudService, JPAPresenter jpaPresenter,
			User currentUser) {
		return new EntityPresenter<>(crudService, currentUser, jpaPresenter);
	}

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public EntityPresenter<User> userEntityPresenter(UserService crudService, JPAPresenter jpaPresenter, User currentUser) {
		return new EntityPresenter<>(crudService, currentUser, jpaPresenter);
	}

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public EntityPresenter<Order> orderEntityPresenter(OrderService crudService, JPAPresenter jpaPresenter, User currentUser) {
		return new EntityPresenter<>(crudService, currentUser, jpaPresenter);
	}

}
