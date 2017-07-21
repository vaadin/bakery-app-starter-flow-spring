package com.vaadin.starter.bakery.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vaadin.starter.bakery.backend.data.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);

	Page<User> findBy(Pageable pageable);

	Page<User> findByEmailLikeIgnoreCaseOrFirstNameLikeIgnoreCaseOrLastNameLikeIgnoreCaseOrRoleLikeIgnoreCase(
			String emailLike, String firstNameLike, String roleLike, Pageable pageable);

	long countByEmailLikeIgnoreCaseOrFirstNameLikeIgnoreCaseOrLastNameLikeIgnoreCase(String emailLike,
			String firstNameLike, String lastNameLike);
}
