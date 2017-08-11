package com.vaadin.starter.bakery.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vaadin.starter.bakery.app.BeanLocator;
import com.vaadin.starter.bakery.app.security.SecurityUtils;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements CrudService<User> {

	public static final String MODIFY_LOCKED_USER_NOT_PERMITTED =
			"User has been locked and cannot be modified or deleted";
	private static final String DELETING_SELF_NOT_PERMITTED = "You cannot delete your own account";
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public UserService(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public User getCurrentUser() {
		return getRepository().findByEmail(SecurityUtils.getUsername());
	}

	@Override
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
	public long countAnyMatching(Optional<String> filter) {
		if (filter.isPresent()) {
			String repositoryFilter = "%" + filter.get() + "%";
			return getRepository().countByEmailLikeIgnoreCaseOrFirstNameLikeIgnoreCaseOrLastNameLikeIgnoreCase(
					repositoryFilter, repositoryFilter, repositoryFilter);
		} else {
			return getRepository().count();
		}
	}

	@Override
	public UserRepository getRepository() {
		return BeanLocator.find(UserRepository.class);
	}

	@Override
	public Page<User> find(Pageable pageable) {
		return getRepository().findBy(pageable);
	}

	public String encodePassword(String value) {
		return passwordEncoder.encode(value);
	}

	@Override
	@Transactional
	public User save(User entity) {
		throwIfUserLocked(entity.getId());
		return getRepository().saveAndFlush(entity);
	}

	@Override
	@Transactional
	public void delete(long userId) {
		throwIfDeletingSelf(userId);
		throwIfUserLocked(userId);
		getRepository().delete(userId);
	}

	private void throwIfDeletingSelf(Long userId) {
		if (userId == null) {
			return;
		}

		User current = getCurrentUser();
		if (current.getId().equals(userId)) {
			throw new UserFriendlyDataException(DELETING_SELF_NOT_PERMITTED);
		}
	}

	private void throwIfUserLocked(Long userId) {
		if (userId == null) {
			return;
		}

		User dbUser = getRepository().findOne(userId);
		if (dbUser.isLocked()) {
			throw new UserFriendlyDataException(MODIFY_LOCKED_USER_NOT_PERMITTED);
		}
	}
}
