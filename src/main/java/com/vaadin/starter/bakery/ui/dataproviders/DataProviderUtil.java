package com.vaadin.starter.bakery.ui.dataproviders;

import java.util.function.Function;
import java.util.function.Supplier;

import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.function.SerializableFunction;

public class DataProviderUtil {

	public static <S, T> T convertIfNotNull(S source, SerializableFunction<S, T> converter) {
		return convertIfNotNull(source, converter, () -> null);
	}

	public static <S, T> T convertIfNotNull(S source, SerializableFunction<S, T> converter, Supplier<T> nullValueSupplier) {
		return source != null ? converter.apply(source) : nullValueSupplier.get();
	}

	public static <T> ItemLabelGenerator<T> createItemLabelGenerator(SerializableFunction<T, String> converter) {
		return item -> convertIfNotNull(item, converter, () -> "");
	}
}
