package com.vaadin.ui.event;

import com.vaadin.flow.component.ComponentEvent;

/**
 * FIXME: remove this when charts is updated to alpha11
 */
@Deprecated
@FunctionalInterface
public interface ComponentEventListener<T extends ComponentEvent<?>>
		extends com.vaadin.flow.component.ComponentEventListener<T> {
}

