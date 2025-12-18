package com.vaadin.starter.bakery.app;

import com.vaadin.flow.component.combobox.ComboBoxBase;
import com.vaadin.starter.bakery.ui.views.orderedit.HistoryItemDisplayData;
import com.vaadin.starter.bakery.ui.views.orderedit.OrderDisplayData;
import com.vaadin.starter.bakery.ui.views.orderedit.OrderItemDisplayData;
import com.vaadin.starter.bakery.ui.views.orderedit.ProductDisplayData;
import com.vaadin.starter.bakery.ui.views.storefront.OrderCard;
import com.vaadin.starter.bakery.ui.views.storefront.beans.OrderCardHeader;
import com.vaadin.starter.bakery.ui.views.storefront.converters.StorefrontDate;
import org.jspecify.annotations.Nullable;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.ReflectionHints;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;

import java.util.Set;

public class NativeBuildConfiguration implements RuntimeHintsRegistrar {
    @Override
    public void registerHints(RuntimeHints hints, @Nullable ClassLoader classLoader) {
        ReflectionHints reflection = hints.reflection();
        reflection.registerTypes(Set.of(
                TypeReference.of("com.vaadin.starter.bakery.backend.data.entity.Order"),
                TypeReference.of("com.vaadin.starter.bakery.backend.data.entity.OrderItem"),
                TypeReference.of("com.vaadin.starter.bakery.backend.data.OrderState"),
                TypeReference.of(StorefrontDate.class),
                TypeReference.of(OrderCardHeader.class),
                TypeReference.of(OrderCard.class),
                TypeReference.of(OrderDisplayData.class),
                TypeReference.of(OrderItemDisplayData.class),
                TypeReference.of(ProductDisplayData.class),
                TypeReference.of(HistoryItemDisplayData.class)
        ), builder -> builder.withMembers(MemberCategory.INVOKE_DECLARED_METHODS, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS));
    }

}
