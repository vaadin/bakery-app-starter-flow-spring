/**
 *
 */
package com.vaadin.starter.bakery.ui.presenter;

import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.ui.event.DecisionEvent;
import com.vaadin.starter.bakery.ui.messages.Message;
import com.vaadin.ui.event.ComponentEventListener;

public interface Confirmer {

	void show(Message message);

	Registration addDecisionListener(ComponentEventListener<DecisionEvent> listener);
}
