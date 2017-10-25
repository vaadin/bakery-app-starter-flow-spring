/**
 *
 */
package com.vaadin.starter.bakery.ui.presenter;

import java.util.Optional;

import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import com.vaadin.starter.bakery.backend.service.FilterableCrudService;

public class DefaultEntityPresenter<T extends AbstractEntity> extends EntityViewPresenter<T> {

	private FilterableCrudService<T> crudService;
	private ListableEntityView<T> view;

	public DefaultEntityPresenter(FilterableCrudService<T> crudService, ListableEntityView<T> view, String entityName) {
		super(crudService, view, entityName);
		this.crudService = crudService;
		this.view = view;
		filter(Optional.empty());
	}

	public void filter(Optional<String> filter) {
		view.list(crudService.findAnyMatching(filter));
	}

	@Override
	protected void onSaveSuccess() {
		filter(Optional.empty());
		super.onSaveSuccess();
	}

	@Override
	protected void onDeleteSuccess() {
		filter(Optional.empty());
		super.onDeleteSuccess();
	}

}
