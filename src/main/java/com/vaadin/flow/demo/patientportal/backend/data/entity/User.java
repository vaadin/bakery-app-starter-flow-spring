package com.vaadin.flow.demo.patientportal.backend.data.entity;

import java.util.Objects;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class User extends AbstractEntity {

	@NotNull
	@NotEmpty
	@Size(max = 255)
	private String email;

	@NotNull
	@NotEmpty
	@Size(min = 4, max = 255)
	private String password;

	@NotNull
	@NotEmpty
	@Size(max = 255)
	private String name;

	@NotNull
	@Size(max = 255)
	private String role;

	public User() {
		// An empty constructor is needed for all beans
	}

	public User(String email, String name, String password, String role) {
		Objects.requireNonNull(email);
		Objects.requireNonNull(name);
		Objects.requireNonNull(password);
		Objects.requireNonNull(role);

		this.email = email;
		this.name = name;
		this.password = password;
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
