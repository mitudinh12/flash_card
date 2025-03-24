package com.flash_card.localization;

import java.util.Locale;
import java.util.ResourceBundle;

public class Localization {
    private ResourceBundle messages;
    private Locale locale;
    private static Localization instance;

    private Localization() {
        this.locale = new Locale("en", "US");
        this.messages = ResourceBundle.getBundle("messages", locale);
    }

    public static Localization getInstance() {
        if (instance == null) {
            instance = new Localization();
        }
        return instance;
    }

    public void setLocaleByLanguage(String langChoice) {
        this.locale = LocaleFactory.getLocaleByLanguage(langChoice);
        this.messages = ResourceBundle.getBundle("messages", locale);
    }

    public String getMessage(String key) {
        return messages.getString(key);
    };

}
