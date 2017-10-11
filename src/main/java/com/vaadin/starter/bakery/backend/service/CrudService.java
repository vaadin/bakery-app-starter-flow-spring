package com.vaadin.starter.bakery.backend.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;

public interface CrudService<T extends AbstractEntity> {

	JpaRepository<T, Long> getRepository();

	default T save(T entity) {
		return getRepository().saveAndFlush(entity);
	}

	default void delete(T entity) {
		getRepository().delete(entity);
	}

	default void delete(long id) {
		delete(load(id));
	}

	default long count() {
		return getRepository().count();
	}

	default T load(long id) {
		T entity = getRepository().findOne(id);
		if (entity == null) {
			throw new EntityNotFoundException();
		}
		return entity;
	}

}
