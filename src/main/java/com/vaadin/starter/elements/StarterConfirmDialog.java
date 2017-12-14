/*
 * Copyright 2000-2017 Vaadin Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.starter.elements;

import com.vaadin.shared.Registration;
import com.vaadin.ui.Component;
import com.vaadin.ui.Tag;
import com.vaadin.ui.common.HtmlImport;
import com.vaadin.ui.event.ComponentEvent;
import com.vaadin.ui.event.ComponentEventListener;
import com.vaadin.ui.event.DomEvent;
import com.vaadin.ui.event.Synchronize;

/**
 * Java implementation of the polymer element
 * `starter-elements/starter-confirm-dialog`
 * 
 * TODO: this hand-written class should be replaced by an auto-generated one
 * when flow provides a maven plugin for generating java classes for 3rd party
 * polymer elements.
 */
@Tag("starter-confirm-dialog")
@HtmlImport("frontend://bower_components/starter-elements/starter-confirm-dialog.html")
public class StarterConfirmDialog extends Component {

	public String getCaption() {
		return getElement().getProperty("caption");
	}

	public void setCaption(String caption) {
		getElement().setProperty("caption", caption == null ? "" : caption);
	}

	public String getMessage() {
		return getElement().getProperty("message");
	}

	public void setMessage(String message) {
		getElement().setProperty("message", message == null ? "" : message);
	}

	public String getOkText() {
		return getElement().getProperty("okText");
	}

	public void setOkText(String okText) {
		getElement().setProperty("okText", okText == null ? "" : okText);
	}

	public String getCancelText() {
		return getElement().getProperty("cancelText");
	}

	public void setCancelText(String cancelText) {
		getElement().setProperty("cancelText", cancelText == null ? "" : cancelText);
	}

	@Synchronize(property = "opened", value = "opened-changed")
	public boolean isOpened() {
		return getElement().getProperty("opened", false);
	}

	public void setOpened(boolean opened) {
		getElement().setProperty("opened", opened);
	}

	@DomEvent("cancel-click")
	public static class CancelClickEvent extends ComponentEvent<StarterConfirmDialog> {
		public CancelClickEvent(StarterConfirmDialog source, boolean fromClient) {
			super(source, fromClient);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Registration addCancelClickListener(
			ComponentEventListener<CancelClickEvent> listener) {
		return addListener(CancelClickEvent.class, (ComponentEventListener) listener);
	}

	@DomEvent("ok-click")
	public static class OkClickEvent extends ComponentEvent<StarterConfirmDialog> {
		public OkClickEvent(StarterConfirmDialog source, boolean fromClient) {
			super(source, fromClient);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Registration addOkClickListener(
			ComponentEventListener<OkClickEvent> listener) {
		return addListener(OkClickEvent.class, (ComponentEventListener) listener);
	}
}