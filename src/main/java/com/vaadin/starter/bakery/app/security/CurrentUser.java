package com.vaadin.starter.bakery.app.security;

import java.io.Serializable;

import com.vaadin.starter.bakery.backend.data.entity.User;

@FunctionalInterface
public interface CurrentUser extends Serializable {

	User getUser();
}
