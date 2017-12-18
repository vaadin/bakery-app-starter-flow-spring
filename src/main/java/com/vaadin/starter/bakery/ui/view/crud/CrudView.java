/**
 *
 */
package com.vaadin.starter.bakery.ui.view.crud;

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
import com.vaadin.starter.bakery.ui.event.CloseDialogEvent;
import com.vaadin.starter.bakery.ui.view.EntityView;
import com.vaadin.starter.bakery.ui.view.admin.DefaultEntityPresenter;
import com.vaadin.starter.bakery.ui.view.admin.EntityEditor;
import com.vaadin.starter.elements.StarterButtonsBar;
import com.vaadin.starter.elements.StarterDialog;
import com.vaadin.ui.common.HasText;
import com.vaadin.ui.grid.Grid;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

public abstract class CrudView<E extends AbstractEntity, T extends TemplateModel> extends PolymerTemplate<T>
		implements HasLogger, EntityView<E>, EntityEditor<E>, HasUrlParameter<Long> {

	protected void setupEventListeners() {
		getGrid().addSelectionListener(e -> {
			e.getFirstSelectedItem().ifPresent(entity -> navigateToEntity(entity.getId().toString()));
			getGrid().deselectAll();
		});
		addListener(CloseDialogEvent.class, e -> getPresenter().cancel());

		getButtonsBar().addAction1Listener(e -> getPresenter().save());
		getButtonsBar().addAction2Listener(e -> getPresenter().cancel());
		getButtonsBar().addAction3Listener(e -> getPresenter().delete());


		getSearchBar().addActionClickListener(e -> getPresenter().createNew());
		getSearchBar().addFilterChangeListener(e -> getPresenter().filter(getSearchBar().getFilter()));

		getSearchBar().setActionText("New " + getEntityName());
	}

	protected abstract DefaultEntityPresenter<E> getPresenter();

	protected abstract String getBasePage();

	protected abstract EntityEditor<E> getEditor();

	protected abstract BeanValidationBinder<E> getBinder();

	protected abstract StarterButtonsBar getButtonsBar();

	protected abstract StarterDialog getDialog();

	protected abstract BakerySearch getSearchBar();

	protected abstract Grid<E> getGrid();

	protected abstract HasText getTitle();

	protected abstract String getEntityName();

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
		getEditor().read(entity);
		getDialog().setOpened(true);
	}

	@Override
	public boolean isDirty() {
		return getEditor().isDirty();
	}

	@Override
	public void write(E entity) throws ValidationException {
		getEditor().write(entity);
	}

	@Override
	public void read(E e) {
		getBinder().readBean(e);
		getButtonsBar().setAction1Disabled(true);
		getButtonsBar().setAction3Disabled(e.isNew());
		getTitle().setText((e.isNew() ? "New" : "Edit") + " " + getEntityName());
	}
}
