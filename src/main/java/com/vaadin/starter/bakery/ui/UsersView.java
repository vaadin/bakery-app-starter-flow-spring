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
package com.vaadin.starter.bakery.ui;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.access.annotation.Secured;

import com.google.gson.Gson;
import com.vaadin.annotations.ClientDelegate;
import com.vaadin.annotations.HtmlImport;
import com.vaadin.annotations.Tag;
import com.vaadin.flow.router.LocationChangeEvent;
import com.vaadin.flow.router.View;
import com.vaadin.flow.template.PolymerTemplate;
import com.vaadin.flow.template.model.TemplateModel;
import com.vaadin.hummingbird.ext.spring.annotations.ParentView;
import com.vaadin.hummingbird.ext.spring.annotations.Route;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.ui.dataproviders.UserDataProvider;
import com.vaadin.starter.bakery.ui.entities.User;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.ui.AttachEvent;

import elemental.json.JsonObject;

/**
 * @author Vaadin Ltd
 */
@Tag("bakery-users")
@HtmlImport("frontend://src/users/bakery-users.html")
@Route(BakeryConst.PAGE_USERS + "/{id}")
@ParentView(BakeryApp.class)
@Secured(Role.ADMIN)
public class UsersView extends PolymerTemplate<UsersView.Model> implements View, HasLogger {

	public interface Model extends TemplateModel {
		void setUsers(List<User> users);

		void setErrorMessage(String message);
	}

	private final UserDataProvider userDataProvider;

	@Autowired
	public UsersView(UserDataProvider userDataProvider) {
		this.userDataProvider = userDataProvider;
	}

	@Override
	public void onLocationChange(LocationChangeEvent locationChangeEvent) {
		setEditableUser(locationChangeEvent.getPathParameter("id"));

	}

	private void setEditableUser(String userId) {
		if (userId == null || userId.isEmpty()) {
			return;
		}

		Long id = null;
		String json = null;
		try {
			id = Long.parseLong(userId);
			json = new Gson().toJson(userDataProvider.findById(id));
		} catch (Exception e) {
			return;
		}
		getElement().callFunction("setEditableUser", json);
	}

	@Override
	protected void onAttach(AttachEvent event) {
		super.onAttach(event);
		getModel().setUsers(userDataProvider.findAll());
	}

	@ClientDelegate
	private void editUser(String userId) {
		if (userId != null && !userId.isEmpty()) {
			getUI().get().navigateTo(BakeryConst.PAGE_USERS + "/" + userId);
		} else {
			getUI().get().navigateTo(BakeryConst.PAGE_USERS);
		}
	}

	@ClientDelegate
	private void saveUser(JsonObject user) {
		try {
			userDataProvider.save(user);
		} catch (DataIntegrityViolationException e) {
			// Commit failed because of validation errors
			getModel().setErrorMessage(e.getMessage());
			getLogger().debug("Data integrity violation error while updating entity of type "
					+ com.vaadin.starter.bakery.backend.data.entity.User.class.getName(), e);
		} catch (OptimisticLockingFailureException e) {
			// Somebody else probably edited the data at the same time
			getModel().setErrorMessage("Somebody else might have updated the data. Please refresh and try again.");
			getLogger().debug("Optimistic locking error while saving entity of type "
					+ com.vaadin.starter.bakery.backend.data.entity.User.class.getName(), e);
		} catch (Exception e) {
			// Something went wrong, no idea what
			getModel().setErrorMessage("A problem occurred while saving the data. Please check the fields.");
			getLogger().error("Unable to save entity of type "
					+ com.vaadin.starter.bakery.backend.data.entity.User.class.getName(), e);
		} finally {
			getModel().setUsers(userDataProvider.findAll());
		}
	}

	@ClientDelegate
	private void deleteUser(String userId) {
		boolean isInSync = false;
		try {
			userDataProvider.delete(userId);
			isInSync = true;
		} catch (DataIntegrityViolationException e) {
			// Commit failed because of validation errors
			getModel().setErrorMessage(
					"The given entity cannot be deleted as there are references to it in the database");
			getLogger().error("Data integrity violation error while deleting entity of type "
					+ com.vaadin.starter.bakery.backend.data.entity.User.class.getName(), e);
		} catch (OptimisticLockingFailureException e) {
			// Somebody else probably edited the data at the same time
			getModel().setErrorMessage("Somebody else might have updated the data. Please refresh and try again.");
			getLogger().debug("Optimistic locking error while deleting entity of type "
					+ com.vaadin.starter.bakery.backend.data.entity.User.class.getName(), e);
		} catch (Exception e) {
			// Something went wrong, no idea what
			getModel().setErrorMessage("A problem occurred while deleting the data. Please refresh and try again.");
			getLogger().error("Unable to delete entity of type "
					+ com.vaadin.starter.bakery.backend.data.entity.User.class.getName(), e);
		} finally {
			if (!isInSync) {
				// re-sync the UI state from the DB if the DB operation fails
				getModel().setUsers(userDataProvider.findAll());
			}
		}
	}
}
