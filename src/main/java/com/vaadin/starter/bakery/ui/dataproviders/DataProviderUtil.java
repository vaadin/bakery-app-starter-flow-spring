package com.vaadin.starter.bakery.ui.dataproviders;

import java.util.function.Supplier;

import org.springframework.cglib.core.internal.Function;

import com.google.gson.Gson;

import elemental.json.JsonObject;

public class DataProviderUtil {

	public static <T> T toUIEntity(JsonObject jsonOrder, Class<T> uiEntityClass) {
		Gson gson = new Gson();
		return gson.fromJson(jsonOrder.toJson(), uiEntityClass);
	}

	public static Long readId(String id) {
		return convertIfNotNull(id,Long::valueOf);
	}

	public static <S, T> T convertIfNotNull(S source, Function<S, T> converter) {
		return convertIfNotNull(source, converter, () -> null);
	}

	public static <S, T> T convertIfNotNull(S source, Function<S, T> converter, Supplier<T> nullValueSupplier) {
		return source != null ? converter.apply(source) : nullValueSupplier.get();
	}

}
