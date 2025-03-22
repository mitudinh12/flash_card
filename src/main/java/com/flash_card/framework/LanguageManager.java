package com.flash_card.framework;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageManager {
    private static Locale locale = Locale.getDefault();
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("messages", locale);

    public static void setLocale(Locale newLocale) {
        locale = newLocale;
        resourceBundle = ResourceBundle.getBundle("messages", locale);
    }

    public static String getString(String key) {
        return resourceBundle.getString(key);
    }

    public static Locale getLocale() {
        return locale;
    }
}
