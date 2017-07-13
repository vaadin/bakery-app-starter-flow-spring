package com.vaadin.flow.demo.patientportal.ui.dataproviders;

import com.google.gson.Gson;
import com.vaadin.flow.demo.patientportal.backend.service.UserService;
import com.vaadin.flow.demo.patientportal.ui.entities.User;
import elemental.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDataProvider {
	private static final String ROLE_BARISTA = "Barista";
	private static final String ROLE_ADMIN = "Admin";
	private static final String ROLE_BAKER = "Baker";

	private final UserService userService;

	@Autowired
	public UserDataProvider(UserService userService) {
		this.userService = userService;
	}

	public List<User> findAll() {
		return userService.getRepository().findAll()
				.stream()
				.map(this::toUIEntity)
				.collect(Collectors.toList());
	}

	public void save(User user) {
		this.userService.save(toDataEntity(user));
	}

	public void save(JsonObject user) {
		this.userService.save(toDataEntity(user));
	}

	public User toUIEntity(com.vaadin.flow.demo.patientportal.backend.data.entity.User dataEntity) {
		User uiEntity = new User();
		uiEntity.setId(dataEntity.getId().toString());
		uiEntity.setName(dataEntity.getName());
		uiEntity.setLast("LastName");
		uiEntity.setEmail(dataEntity.getEmail());
		uiEntity.setPassword(null); // do not send the password to the UI
		uiEntity.setPicture("https://randomuser.me/api/portraits/women/10.jpg");

		switch (dataEntity.getRole().toLowerCase()) {
			case "barista":
				uiEntity.setRole(ROLE_BARISTA);
				break;
			case "admin":
				uiEntity.setRole(ROLE_ADMIN);
				break;
			case "baker":
				uiEntity.setRole(ROLE_BAKER);
				break;
		}

		return uiEntity;
	}

	public com.vaadin.flow.demo.patientportal.backend.data.entity.User toDataEntity(User uiEntity) {
		com.vaadin.flow.demo.patientportal.backend.data.entity.User dataEntity = null;
		try {
			long id = Long.parseLong(uiEntity.getId());
			dataEntity = userService.load(id);
		} catch (NumberFormatException e) {
			// ok to ignore?
		}

		if (dataEntity == null) {
			dataEntity = new com.vaadin.flow.demo.patientportal.backend.data.entity.User();
		}

		dataEntity.setName(uiEntity.getName());
		dataEntity.setEmail(uiEntity.getEmail());
		if (uiEntity.getPassword() != null) {
			dataEntity.setPassword(userService.encodePassword(uiEntity.getPassword()));
		}

		switch (uiEntity.getRole()) {
		case ROLE_BARISTA:
			dataEntity.setRole("barista");
			break;
		case ROLE_ADMIN:
			dataEntity.setRole("admin");
			break;
		case ROLE_BAKER:
			dataEntity.setRole("baker");
			break;
		}

		return dataEntity;
	}

	public com.vaadin.flow.demo.patientportal.backend.data.entity.User toDataEntity(JsonObject user) {
		Gson gson = new Gson();
		User uiEntity = gson.fromJson(user.toJson(), User.class);
		return toDataEntity(uiEntity);
	}

	public void delete(String userId) {
		this.userService.delete(Long.parseLong(userId));
	}

	public UserService getService() {
		return userService;
	}
}
