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
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.demo.patientportal.backend.data.Role;
import com.vaadin.flow.demo.patientportal.backend.service.UserService;
import com.vaadin.flow.demo.patientportal.ui.utils.BakeryConst;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.hummingbird.ext.spring.annotations.ParentView;
import com.vaadin.hummingbird.ext.spring.annotations.Route;
import com.vaadin.ui.AttachEvent;

/**
 * @author Vaadin Ltd
 */
@Tag("bakery-users")
@HtmlImport("frontend://src/users/bakery-users.html")
@Route(BakeryConst.PAGE_USERS)
@ParentView(BakeryApp.class)
@Secured(Role.ADMIN)
public class UsersView extends PolymerTemplate<UsersView.Model> implements View {

    public static class UserModel {
        private String name;
        private String last;
        private String email;
        private String picture;
        private String role;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLast() {
            return last;
        }

        public void setLast(String last) {
            this.last = last;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }

    public interface Model extends TemplateModel {
        void setUsers(List<UserModel> users);
    }

    private UserService userService;

    @Autowired
    public UsersView(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void onAttach(AttachEvent event) {
        super.onAttach(event);

        List<UserModel> users = userService.getRepository().findAll().stream().map(user -> {
            UserModel model = new UserModel();
            model.setName(user.getName());
            model.setLast("McLast");
            model.setEmail(user.getEmail());
            model.setPicture("https://randomuser.me/api/portraits/women/10.jpg");
            model.setRole(user.getRole());
            return model;
        }).collect(Collectors.toList());
        getModel().setUsers(users);
    }
}
