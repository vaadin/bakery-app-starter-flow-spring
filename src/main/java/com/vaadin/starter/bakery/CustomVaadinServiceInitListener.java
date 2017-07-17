package com.vaadin.starter.bakery;

import com.vaadin.server.ServiceInitEvent;
import com.vaadin.server.VaadinServiceInitListener;

public class CustomVaadinServiceInitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.addBootstrapListener(new CustomBootstrapListener());
    }
}