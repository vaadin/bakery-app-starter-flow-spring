package com.vaadin.starter.bakery.ui.views.admin.users;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.PAGE_USERS;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.ui.MainView;
import com.vaadin.starter.bakery.ui.components.GridComponent;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;

@Route(value = PAGE_USERS, layout = MainView.class)
@PageTitle(BakeryConst.TITLE_USERS)
@RolesAllowed({Role.ADMIN, Role.BARISTA})
//@AnyOfficeRoleAllowed
public class UsersView extends VerticalLayout {

	@Autowired
	private GridComponent grid;

	@PostConstruct
	public void init() {
		add(grid);
	}
}
