package com.vaadin.starter.bakery.ui.view;

import com.vaadin.flow.model.TemplateModel;
import com.vaadin.flow.router.View;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.ui.UsersView.Model;
import com.vaadin.starter.bakery.ui.components.ConfirmationDialog;
import com.vaadin.starter.bakery.ui.components.ItemsView;
import com.vaadin.starter.bakery.ui.presenter.EntityView;
import com.vaadin.ui.polymertemplate.Id;
import com.vaadin.ui.polymertemplate.PolymerTemplate;
import com.vaadin.ui.polymertemplate.TemplateParser;

public abstract class DefaultEntityView<ENTITY extends AbstractEntity, MODEL extends TemplateModel>
extends PolymerTemplate<MODEL> implements EntityView<User> {

	@Id("bakery-users-items-view")
	protected ItemsView view;

	@Id("user-confirmation-dialog")
	protected ConfirmationDialog confirmationDialog;

}