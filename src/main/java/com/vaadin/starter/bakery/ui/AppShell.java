package com.vaadin.starter.bakery.ui;

import com.vaadin.flow.component.page.VaadinAppShell;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.server.PWA;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.VIEWPORT;

@Viewport(VIEWPORT)
@PWA(name = "Bakery App Starter", shortName = "###Bakery###",
		startPath = "login",
		backgroundColor = "#227aef", themeColor = "#227aef",
		offlinePath = "offline-page.html",
		offlineResources = {"images/offline-login-banner.jpg"})
public class AppShell extends VaadinAppShell {
}
