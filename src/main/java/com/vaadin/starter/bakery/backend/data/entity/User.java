package com.vaadin.starter.bakery.backend.data.entity;

import java.util.Objects;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity(name="UserInfo")
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

	@Size(max = 2083)
	private String photoUrl;

	private boolean locked = false;

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

	public User(String email, String firstName, String lastName, String password, String role, String photoUrl) {
		this(email, firstName, lastName, password, role);
		this.photoUrl = photoUrl;
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

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}
}
