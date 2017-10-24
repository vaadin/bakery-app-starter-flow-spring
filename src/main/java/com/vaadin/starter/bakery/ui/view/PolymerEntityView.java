/**
 *
 */
package com.vaadin.starter.bakery.ui.view;

import java.util.Optional;

import com.vaadin.data.ValidationException;
import com.vaadin.flow.model.TemplateModel;
import com.vaadin.router.HasUrlParameter;
import com.vaadin.router.OptionalParameter;
import com.vaadin.router.event.BeforeNavigationEvent;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import com.vaadin.starter.bakery.ui.components.ConfirmationDialog;
import com.vaadin.starter.bakery.ui.components.ItemsView;
import com.vaadin.starter.bakery.ui.event.CancelEvent;
import com.vaadin.starter.bakery.ui.event.CloseDialogEvent;
import com.vaadin.starter.bakery.ui.event.DeleteEvent;
import com.vaadin.starter.bakery.ui.event.EditEvent;
import com.vaadin.starter.bakery.ui.event.SaveEvent;
import com.vaadin.starter.bakery.ui.presenter.DefaultEntityPresenter;
import com.vaadin.starter.bakery.ui.presenter.ListableEntityView;
import com.vaadin.ui.polymertemplate.PolymerTemplate;

public abstract class PolymerEntityView<E extends AbstractEntity, T extends TemplateModel> extends PolymerTemplate<T>
implements HasLogger, ListableEntityView<E>, HasUrlParameter<Long> {

	protected void setupEventListeners() {
		addListener(EditEvent.class, e -> navigateToEntity(e.getId()));
		addListener(CloseDialogEvent.class, e -> getPresenter().cancel());
		getEditor().addListener(CancelEvent.class, e -> getPresenter().cancel());
		getEditor().addListener(SaveEvent.class, e -> getPresenter().save());
		getEditor().addListener(DeleteEvent.class, e -> getPresenter().delete());
		getConfirmer().addDecisionListener(getPresenter()::confirmationDecisionReceived);
		getItemsView().addActionClickListener(e -> getPresenter().createNew());
		getItemsView().addFilterChangeListener(f -> getPresenter().filter(Optional.ofNullable(f)));
	}

	public abstract ConfirmationDialog getConfirmer();

	protected abstract DefaultEntityPresenter<E> getPresenter();

	protected abstract String getBasePage();

	protected abstract EntityEditor<E> getEditor();

	protected abstract ItemsView getItemsView();

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
	public void closeDialog(boolean updated) {
		getItemsView().openDialog(false);
		navigateToEntity(null);
	}

	@Override
	public void openDialog(E entity, boolean edit) {
		getEditor().read(entity);
		getItemsView().openDialog(true);
	}

	@Override
	public boolean isDirty() {
		return getEditor().isDirty();
	}

	@Override
	public void write(E entity) throws ValidationException {
		getEditor().write(entity);
	}
}
