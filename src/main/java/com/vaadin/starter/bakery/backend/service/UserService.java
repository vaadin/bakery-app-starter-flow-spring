package com.vaadin.starter.bakery.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.starter.bakery.app.security.SecurityUtils;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.repositories.UserRepository;

@Service
public class UserService implements CrudService<User> {

	public static final String MODIFY_LOCKED_USER_NOT_PERMITTED =
			"User has been locked and cannot be modified or deleted";
	private static final String DELETING_SELF_NOT_PERMITTED = "You cannot delete your own account";
	private final UserRepository userRepository;


	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User getCurrentUser() {
		return getRepository().findByEmail(SecurityUtils.getUsername());
	}

	public Page<User> findAnyMatching(Optional<String> filter, Pageable pageable) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return getRepository()
					.findByEmailLikeIgnoreCaseOrFirstNameLikeIgnoreCaseOrLastNameLikeIgnoreCaseOrRoleLikeIgnoreCase(
							repositoryFilter, repositoryFilter, repositoryFilter, repositoryFilter, pageable);
		} else {
			return find(pageable);
		}
	}

	@Override
	public UserRepository getRepository() {
		return userRepository;
	}

	public Page<User> find(Pageable pageable) {
		return getRepository().findBy(pageable);
	}

	@Override
	@Transactional
	public User save(User entity) {
		throwIfUserLocked(entity);
		return getRepository().saveAndFlush(entity);
	}

	@Override
	@Transactional
	public void delete(User user) {
		throwIfDeletingSelf(user);
		throwIfUserLocked(user);
		CrudService.super.delete(user);
	}

	private void throwIfDeletingSelf(User user) {
		User current = getCurrentUser();
		if (current.equals(user)) {
			throw new UserFriendlyDataException(DELETING_SELF_NOT_PERMITTED);
		}
	}

	private void throwIfUserLocked(User entity) {
		if (entity.isLocked()) {
			throw new UserFriendlyDataException(MODIFY_LOCKED_USER_NOT_PERMITTED);
		}
	}

	@Override
	public User createNew() {
		return new User();
	}
}
