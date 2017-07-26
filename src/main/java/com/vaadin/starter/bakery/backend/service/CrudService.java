package com.vaadin.starter.bakery.backend.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;

public interface CrudService<T extends AbstractEntity> {

	JpaRepository<T, Long> getRepository();

	default T save(T entity) {
		return getRepository().saveAndFlush(entity);
	}

	default void delete(long id) {
		getRepository().delete(id);
	}

	default long count() {
		return getRepository().count();
	}

	default T load(long id) {
		return getRepository().findOne(id);
	}

	long countAnyMatching(Optional<String> filter);

	Page<T> findAnyMatching(Optional<String> filter, Pageable pageable);

	Page<T> find(Pageable pageable);
}
