package com.vaadin.starter.elements;

import com.vaadin.shared.Registration;
import com.vaadin.ui.Component;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HasStyle;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.event.ComponentEvent;
import com.vaadin.ui.event.ComponentEventListener;
import com.vaadin.ui.event.DomEvent;

/**
 * Java implementation of the polymer element
 * `starter-elements/starter-buttons-bar`
 * 
 * TODO: this hand-written class should be replaced by an auto-generated one
 * when flow provides a maven plugin for generating java classes for 3rd party
 * polymer elements.
 */
@Tag("starter-buttons-bar")
@HtmlImport("frontend://bower_components/starter-elements/starter-buttons-bar.html")
public class StarterButtonsBar extends Component implements HasStyle {

	public String getAction1Text() {
		return getElement().getProperty("action1Text");
	}

	public void setAction1Text(String action1Text) {
		getElement().setProperty("action1Text",
				action1Text == null ? "" : action1Text);
	}

	public String getAction2Text() {
		return getElement().getProperty("action2Text");
	}

	public void setAction2Text(String action2Text) {
		getElement().setProperty("action2Text",
				action2Text == null ? "" : action2Text);
	}

	public String getAction3Text() {
		return getElement().getProperty("action3Text");
	}

	public void setAction3Text(String action3Text) {
		getElement().setProperty("action3Text",
				action3Text == null ? "" : action3Text);
	}

	public boolean isAction1Disabled() {
		return getElement().getProperty("action1Disabled", false);
	}

	public void setAction1Disabled(boolean action1Disabled) {
		getElement().setProperty("action1Disabled", action1Disabled);
	}

	public boolean isAction2Disabled() {
		return getElement().getProperty("action2Disabled", false);
	}

	public void setAction2Disabled(boolean action2Disabled) {
		getElement().setProperty("action2Disabled", action2Disabled);
	}

	public boolean isAction3Disabled() {
		return getElement().getProperty("action3Disabled", false);
	}

	public void setAction3Disabled(boolean action3Disabled) {
		getElement().setProperty("action3Disabled", action3Disabled);
	}


	@DomEvent("action1")
	public static class Action1Event extends ComponentEvent {
		public Action1Event(StarterButtonsBar source, boolean fromClient) {
			super(source, fromClient);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Registration addAction1Listener(ComponentEventListener listener) {
		return addListener(Action1Event.class,
				listener);
	}

	@DomEvent("action2")
	public static class Action2Event extends ComponentEvent {
		public Action2Event(StarterButtonsBar source, boolean fromClient) {
			super(source, fromClient);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Registration addAction2Listener(ComponentEventListener listener) {
		return addListener(Action2Event.class,
				listener);
	}

	@DomEvent("action3")
	public static class Action3Event extends ComponentEvent {
		public Action3Event(StarterButtonsBar source, boolean fromClient) {
			super(source, fromClient);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Registration addAction3Listener(ComponentEventListener listener) {
		return addListener(Action3Event.class,
				listener);
	}
}