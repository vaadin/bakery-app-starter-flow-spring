package com.vaadin.starter.bakery.ui.dataproviders;

import java.util.function.Function;
import java.util.function.Supplier;

import com.google.gson.Gson;

import elemental.json.JsonObject;

public class DataProviderUtil {

	public static <T> T toUIEntity(JsonObject json, Class<T> uiEntityClass) {
		Gson gson = new Gson();
		return gson.fromJson(json.toJson(), uiEntityClass);
	}

	public static Long readId(String id) {
		return convertIfNotNull(id, Long::valueOf);
	}

	public static <S, T> T convertIfNotNull(S source, Function<S, T> converter) {
		return convertIfNotNull(source, converter, () -> null);
	}

	public static <S, T> T convertIfNotNull(S source, Function<S, T> converter, Supplier<T> nullValueSupplier) {
		return source != null ? converter.apply(source) : nullValueSupplier.get();
	}

}
