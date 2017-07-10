/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.flow.demo.patientportal.ui;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.demo.patientportal.backend.data.entity.User;
import com.vaadin.flow.demo.patientportal.backend.service.UserService;
import com.vaadin.flow.html.Div;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.hummingbird.ext.spring.annotations.ParentView;
import com.vaadin.hummingbird.ext.spring.annotations.Route;

/**
 * @author Vaadin Ltd
 *
 */
@Tag("users-view")
@HtmlImport("frontend://components/users-view.html")
@Route(value = "users")
@ParentView(MainView.class)
//
//@Secured(Role.ADMIN) Secured annotation does not work.
public class UsersView extends PolymerTemplate<UsersView.UsersModel>
        implements View {

    UserService userService;
    @Autowired
    public UsersView( UserService userService) {
        this.userService = userService;
        List<User> users =userService.getRepository().findAll();
        getModel().setNUsers(users.size());
        for(User user: users) {
            Div div = new Div();
            div.setText(user.getName()+" "+user.getEmail());
            getElement().appendChild(div.getElement());
        }
    }
    public static interface UsersModel extends TemplateModel {
        void setNUsers(int NUsers);

    }
}
