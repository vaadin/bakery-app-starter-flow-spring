package com.vaadin.starter.bakery.ui.components;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;

@Tag("user-avatar")
@HtmlImport("frontend://src/elements/user-avatar.html")
public class UserAvatar extends PolymerTemplate<TemplateModel>  {

	public void setSrc(String src) {
		getElement().setProperty("src", src);
	}
}
