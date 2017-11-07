package com.vaadin.starter.bakery.backend.data;

import java.util.Locale;

import com.vaadin.shared.util.SharedUtil;

public class StringUtil {
	
    public static String upperCaseUnderscoreToHumanFriendly(
            String upperCaseUnderscoreString) {
        String[] parts = upperCaseUnderscoreString.replaceFirst("^_*", "")
                .split("_");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = SharedUtil.capitalize(parts[i].toLowerCase(Locale.ROOT));
        }
        return SharedUtil.join(parts, " ");
    }
}
