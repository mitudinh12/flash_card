package com.flash_card;

import com.flash_card.framework.LanguageManager;
import com.flash_card.view.auth.LoginView;

import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        // Set the default locale to English
        LanguageManager.setLocale(new Locale("en", "US"));

        LoginView.launch(LoginView.class);
    }
}
