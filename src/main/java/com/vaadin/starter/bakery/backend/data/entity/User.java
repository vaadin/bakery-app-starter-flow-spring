package com.vaadin.starter.bakery.backend.data.entity;

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
	private String firstName;

	@NotNull
	@NotEmpty
	@Size(max = 255)
	private String lastName;

	@NotNull
	@Size(max = 255)
	private String role;

	public User() {
		// An empty constructor is needed for all beans
	}

	public User(String email, String firstName, String lastName, String password, String role) {
		Objects.requireNonNull(email);
		Objects.requireNonNull(firstName);
		Objects.requireNonNull(lastName);
		Objects.requireNonNull(password);
		Objects.requireNonNull(role);

		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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
