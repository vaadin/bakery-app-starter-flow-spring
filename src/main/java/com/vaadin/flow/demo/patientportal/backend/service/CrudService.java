package com.vaadin.flow.demo.patientportal.backend.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.vaadin.flow.demo.patientportal.backend.data.entity.AbstractEntity;

public interface CrudService<T extends AbstractEntity> {

	CrudRepository<T, Long> getRepository();

	default T save(T entity) {
		return getRepository().save(entity);
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
