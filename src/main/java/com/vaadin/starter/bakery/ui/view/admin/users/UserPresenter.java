package com.vaadin.starter.bakery.ui.view.admin.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.service.FilterableCrudService;
import com.vaadin.starter.bakery.ui.crud.DefaultEntityPresenter;
import com.vaadin.starter.bakery.ui.view.EntityPresenter;

@SpringComponent
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserPresenter extends DefaultEntityPresenter<User> {

	@Autowired
	public UserPresenter(EntityPresenter<User> entityPresenter, FilterableCrudService<User> crudService) {
		super(entityPresenter, crudService, "User");
	}

}
