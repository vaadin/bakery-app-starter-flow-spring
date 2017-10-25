package com.vaadin.starter.bakery.backend.service;

import java.util.List;
import java.util.Optional;

import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;

public interface FilterableCrudService<T extends AbstractEntity> extends CrudService<T> {

	public List<T> findAnyMatching(Optional<String> filter);

}
