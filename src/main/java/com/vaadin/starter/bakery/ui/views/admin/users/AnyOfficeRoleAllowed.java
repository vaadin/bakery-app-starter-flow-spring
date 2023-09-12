package com.vaadin.starter.bakery.ui.views.admin.users;

import javax.annotation.security.RolesAllowed;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.vaadin.starter.bakery.backend.data.Role;

@RolesAllowed(
        {Role.ADMIN}
)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AnyOfficeRoleAllowed {

}
