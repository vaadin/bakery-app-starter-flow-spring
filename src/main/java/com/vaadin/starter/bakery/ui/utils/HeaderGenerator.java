package com.vaadin.starter.bakery.ui.utils;

import java.time.LocalDate;
import java.util.function.Predicate;

import com.vaadin.starter.bakery.ui.entities.StorefrontItemHeader;

class HeaderGenerator {
	private Predicate<LocalDate> matcher;

	private StorefrontItemHeader header;

	private Long selected;

	public HeaderGenerator(Predicate<LocalDate> matcher, StorefrontItemHeader header) {
		this.matcher = matcher;
		this.header = header;
	}

	public boolean matches(LocalDate date) {
		return matcher.test(date);
	}

	public Long getSelected() {
		return selected;
	}

	public void setSelected(Long selected) {
		this.selected = selected;
	}

	public StorefrontItemHeader getHeader() {
		return header;
	}

}