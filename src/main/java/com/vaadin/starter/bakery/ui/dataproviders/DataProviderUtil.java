package com.vaadin.starter.bakery.ui.dataproviders;

import com.google.gson.Gson;

import elemental.json.JsonObject;

public class DataProviderUtil {
	
	public static <T> T toUIEntity(JsonObject jsonOrder,Class<T> uiEntityClass) {
		Gson gson = new Gson();
		return gson.fromJson(jsonOrder.toJson(), uiEntityClass);
	}
	
	public static Long readId(String id) {
		return id != null && id.length() > 0 ?Long.valueOf(id):null;
	}

}
