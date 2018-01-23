package com.vaadin.starter.bakery.ui.components;

import java.util.List;

import com.vaadin.flow.component.ClientDelegate;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.starter.bakery.ui.entities.PageInfo;

@Tag("app-navigation")
@HtmlImport("src/components/app-navigation.html")
public class AppNavigation extends PolymerTemplate<AppNavigation.Model> implements AfterNavigationObserver {

	public interface Model extends TemplateModel {
		void setPageNumber(int page);

		void setPages(List<PageInfo> pages);
	}

	private List<PageInfo> pages;
	private String logoutHref;
	private String defaultHref;

	public void init(List<PageInfo> pages, String defaultHref, String logoutHref) {
		this.pages = pages;
		this.defaultHref = defaultHref;
		this.logoutHref = logoutHref;
		getModel().setPages(pages);
	}

	@ClientDelegate
	private void navigateTo(String href) {
		if (href.equals(logoutHref)) {
			// The logout button is a 'normal' URL, not Flow-managed but
			// handled by Spring Security.
			UI.getCurrent().getPage().executeJavaScript("location.assign('logout')");
		} else {
			UI.getCurrent().navigateTo(href);
		}
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		String currentPath = event.getLocation().getFirstSegment().isEmpty() ? defaultHref
				: event.getLocation().getFirstSegment();

		for (int i = 0; i < pages.size(); i++) {
			if (pages.get(i).getLink().equals(currentPath)) {
				this.getModel().setPageNumber(i);
			}
		}
	}
}
