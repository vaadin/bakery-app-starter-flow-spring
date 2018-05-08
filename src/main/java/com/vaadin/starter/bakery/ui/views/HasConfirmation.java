package com.vaadin.starter.bakery.ui.views;

import com.vaadin.starter.bakery.ui.components.ConfirmDialog;

public interface HasConfirmation {

	void setConfirmDialog(ConfirmDialog confirmDialog);

	ConfirmDialog getConfirmDialog();
}
