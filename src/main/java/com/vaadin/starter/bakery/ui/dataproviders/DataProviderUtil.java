package com.vaadin.starter.bakery.ui.dataproviders;

import java.util.function.Function;
import java.util.function.Supplier;

public class DataProviderUtil {

	public static <S, T> T convertIfNotNull(S source, Function<S, T> converter) {
		return convertIfNotNull(source, converter, () -> null);
	}

	public static <S, T> T convertIfNotNull(S source, Function<S, T> converter, Supplier<T> nullValueSupplier) {
		return source != null ? converter.apply(source) : nullValueSupplier.get();
	}

	/**
	 *
	 * @param object
	 *            object to be transformed
	 * @return {@link Object#toString()} if object is not null. NUll otherwise.
	 */
	public static String toString(Object object) {
		return object != null ? object.toString() : null;
	}
}
