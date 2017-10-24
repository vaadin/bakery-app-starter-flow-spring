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

	private String basePage;

	private ConfirmationDialog confirmer;

	private DefaultEntityPresenter<E> presenter;

	private EntityEditor<E> editor;

	private ItemsView itemsView;

	protected void setup(String basePage, DefaultEntityPresenter<E> presenter, EntityEditor<E> editor,
			ConfirmationDialog confirmer,
			ItemsView itemsView, String actionText) {
		this.basePage = basePage;
		this.confirmer = confirmer;
		this.presenter = presenter;
		this.editor = editor;
		this.itemsView = itemsView;
		addListener(EditEvent.class, e -> navigateToEntity(e.getId()));
		addListener(CloseDialogEvent.class, e -> presenter.cancel());
		editor.addListener(CancelEvent.class, e -> presenter.cancel());
		editor.addListener(SaveEvent.class, e -> presenter.save());
		editor.addListener(DeleteEvent.class, e -> presenter.delete());
		confirmer.addDecisionListener(presenter::confirmationDecisionReceived);
		itemsView.setActionText(actionText);
		itemsView.addActionClickListener(e -> presenter.createNew());
		itemsView.addFilterChangeListener(f -> presenter.filter(Optional.ofNullable(f)));
	}

	public ConfirmationDialog getConfirmer() {
		return confirmer;
	}

	protected void navigateToEntity(String id) {
		final String location = basePage + (id == null || id.isEmpty() ? "" : "/" + id);
		getUI().ifPresent(ui -> ui.navigateTo(location));
	}

	@Override
	public void setParameter(BeforeNavigationEvent event, @OptionalParameter Long id) {
		if (id != null) {
			presenter.loadEntity(id, true);
		}
	}

	@Override
	public void closeDialog(boolean updated) {
		itemsView.openDialog(false);
		navigateToEntity(null);
	}

	@Override
	public void openDialog(E entity, boolean edit) {
		editor.read(entity);
		itemsView.openDialog(true);
	}

	@Override
	public boolean isDirty() {
		return editor.isDirty();
	}

	@Override
	public void write(E entity) throws ValidationException {
		editor.write(entity);
	}
}
