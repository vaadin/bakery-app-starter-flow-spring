package com.vaadin.starter.bakery.ui;

import com.vaadin.flow.router.HasChildView;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;

public abstract class ParentPolymerTemplate<M extends TemplateModel> extends PolymerTemplate<M>
		implements HasChildView {

	protected View childView;

	@Override
	public void setChildView(View childView) {
		if (this.childView != null) {
			getElement().removeChild(this.childView.getElement());
		}
		this.childView = childView;

		if (this.childView != null) {
			getElement().appendChild(this.childView.getElement());
		}
	}
}
