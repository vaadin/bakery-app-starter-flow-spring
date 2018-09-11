package com.vaadin.starter.bakery.app.security;

import com.vaadin.starter.bakery.backend.data.entity.User;

@FunctionalInterface
public interface CurrentUser {

	User getUser();
}
