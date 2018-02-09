package com.vaadin.starter.bakery.ui.components;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.IronIcon;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.starter.bakery.ui.entities.PageInfo;

@Tag("app-navigation")
@HtmlImport("src/components/app-navigation.html")
public class AppNavigation extends PolymerTemplate<AppNavigation.Model> implements AfterNavigationObserver {

	public interface Model extends TemplateModel {
	}

	private String logoutHref;
	private String defaultHref;

	@Id("tabs")
	private Tabs tabs;

	private List<String> links = new ArrayList<>();

	public void init(List<PageInfo> pages, String defaultHref, String logoutHref) {
		for (PageInfo page : pages) {
			Tab tab = new Tab(new Div(new IronIcon("vaadin", page.getIcon()), new Span(page.getTitle())));
			links.add(page.getLink());
			tabs.add(tab);
		}

		tabs.addSelectedChangeListener(e -> navigate());
	}

	private void navigate() {
		String href = links.get(tabs.getSelectedIndex());
		if (href != null) {
			if (href.equals(logoutHref)) {
				// The logout button is a 'normal' URL, not Flow-managed but
				// handled by Spring Security.
				UI.getCurrent().getPage().executeJavaScript("location.assign('logout')");
			} else {
				UI.getCurrent().navigateTo(href);
			}
		}
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		String path = event.getLocation().getFirstSegment().isEmpty() ? defaultHref
				: event.getLocation().getFirstSegment();
		tabs.setSelectedIndex(links.indexOf(path));
	}
}
