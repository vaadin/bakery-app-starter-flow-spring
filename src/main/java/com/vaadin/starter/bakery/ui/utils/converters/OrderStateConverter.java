package com.vaadin.starter.bakery.ui.utils.converters;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.flow.model.ModelConverter;
import com.vaadin.starter.bakery.backend.data.OrderState;

import static com.vaadin.starter.bakery.ui.dataproviders.DataProviderUtil.convertIfNotNull;

public class OrderStateConverter implements Converter<String, OrderState>, ModelConverter<OrderState, String> {

	private Map<String, OrderState> values;

	public OrderStateConverter() {
		values = Arrays.stream(OrderState.values())
				.collect(Collectors.toMap(OrderState::getDisplayName, Function.identity()));
	}

	@Override
	public Result<OrderState> convertToModel(String value, ValueContext context) {
		return Result.ok(toModel(value));
	}

	@Override
	public String convertToPresentation(OrderState value, ValueContext context) {
		return toPresentation(value);
	}

	@Override
	public OrderState toModel(String presentationValue) {
		return convertIfNotNull(presentationValue, values::get);
	}

	@Override
	public String toPresentation(OrderState modelValue) {
		return convertIfNotNull(modelValue, OrderState::getDisplayName);
	}

}
