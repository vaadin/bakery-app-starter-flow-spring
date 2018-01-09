/**
 *
 */
package com.vaadin.starter.bakery.ui.crud;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.router.HasUrlParameter;
import com.vaadin.router.OptionalParameter;
import com.vaadin.router.event.BeforeNavigationEvent;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import com.vaadin.starter.bakery.ui.components.BakerySearch;
import com.vaadin.starter.bakery.ui.components.FormButtonsBar;
import com.vaadin.starter.bakery.ui.components.FormDialog;
import com.vaadin.starter.bakery.ui.view.EntityView;
import com.vaadin.ui.common.HasText;
import com.vaadin.ui.grid.Grid;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

public abstract class CrudView<E extends AbstractEntity, T extends TemplateModel> extends PolymerTemplate<T>
		implements HasLogger, EntityView<E>, HasUrlParameter<Long> {

	private String entityName;
	
	protected abstract DefaultEntityPresenter<E> getPresenter();

	protected abstract String getBasePage();

	protected abstract BeanValidationBinder<E> getBinder();

	protected abstract FormButtonsBar getButtons();

	protected abstract FormDialog getDialog();

	protected abstract BakerySearch getSearchBar();

	protected abstract Grid<E> getGrid();

	protected abstract HasText getTitle();


	@Override
	public void setDataProvider(DataProvider<E, ?> dataProvider) {
		getGrid().setDataProvider(dataProvider);
	}

	protected void navigateToEntity(String id) {
		final String location = getBasePage() + (id == null || id.isEmpty() ? "" : "/" + id);
		getUI().ifPresent(ui -> ui.navigateTo(location));
	}

	@Override
	public void setParameter(BeforeNavigationEvent event, @OptionalParameter Long id) {
		if (id != null) {
			getPresenter().loadEntity(id, true);
		}
	}

	@Override
	public void closeDialog() {
		getDialog().setOpened(false);
		navigateToEntity(null);
	}

	@Override
	public void openDialog(E entity, boolean edit) {
		read(entity);
		getDialog().setOpened(true);
	}

	@Override
	public void write(E entity) throws ValidationException {
		getBinder().writeBean(entity);
	}

	@Override
	public boolean isDirty() {
		return getBinder().hasChanges();
	}

	public void read(E e) {
		getBinder().readBean(e);
		getButtons().setSaveDisabled(true);
		getButtons().setDeleteDisabled(e.isNew());
		getTitle().setText((e.isNew() ? "New" : "Edit") + " " + entityName);
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	
}
