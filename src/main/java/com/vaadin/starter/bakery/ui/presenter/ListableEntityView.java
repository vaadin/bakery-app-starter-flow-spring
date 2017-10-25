package com.vaadin.starter.bakery.ui.presenter;

import java.util.List;

public interface ListableEntityView<T> extends EntityView<T> {

	void list(List<T> entities);
}