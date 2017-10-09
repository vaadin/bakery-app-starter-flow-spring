package com.vaadin.starter.bakery.ui.components.viewselector;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.event.ComponentEventListener;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.shared.Registration;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.service.ProductService;
import com.vaadin.starter.bakery.backend.service.UserService;
import com.vaadin.starter.bakery.ui.components.storefront.OrderDetail;
import com.vaadin.starter.bakery.ui.components.storefront.OrderEdit;
import com.vaadin.ui.ComponentEvent;

import java.util.Collection;

import static com.vaadin.starter.bakery.ui.utils.TemplateUtil.addToSlot;

@Tag("view-selector")
@HtmlImport("context://src/elements/viewselector/view-selector.html")
public class ViewSelector extends PolymerTemplate<ViewSelector.Model> {

	public interface Model extends TemplateModel {
		void setOpened(boolean opened);
	}


}
