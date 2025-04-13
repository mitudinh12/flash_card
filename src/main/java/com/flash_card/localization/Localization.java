package com.flash_card.localization;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.ResourceBundle;

public final class Localization {
    /**
     * Singleton instance of the Localization class.
     */
    private static Localization instance;
    /**
     * ResourceBundle containing localized messages.
     */
    private ResourceBundle messages;
    /**
     * Current locale used for localization.
     */
    private Locale locale;
    /**
     * Private constructor to initialize the default locale and messages.
     */
    private Localization() {
        this.locale = new Locale("en", "US");
        this.messages = ResourceBundle.getBundle("messages", locale);
    }
    /**
     * Returns the singleton instance of the Localization class.
     *
     * @return the singleton instance
     */
    public static Localization getInstance() {
        if (instance == null) {
            instance = new Localization();
        }
        return instance;
    }
    /**
     * Sets the locale based on the provided language choice and updates the messages.
     *
     * @param langChoice the language code (e.g., "en", "fi", "ko")
     */
    public void setLocaleByLanguage(final String langChoice) {
        this.locale = LocaleFactory.getLocaleByLanguage(langChoice);
        this.messages = ResourceBundle.getBundle("messages", locale);
    }
    /**
     * Returns the ResourceBundle containing localized messages.
     *
     * @return the ResourceBundle with localized messages
     */
    public ResourceBundle getBundle() {
        return messages;
    }
    /**
     * Returns a DecimalFormat object configured for the current locale.
     *
     * @return a DecimalFormat object for the current locale
     */
    public DecimalFormat getNumberFormat() {
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(locale);
        DecimalFormat decimalFormat = new DecimalFormat("#0.00", symbols);
        return decimalFormat;
    }
    /**
     * Retrieves a localized message by its key.
     *
     * @param key the key for the desired message
     * @return the localized message
     */
    public String getMessage(final String key) {
        return messages.getString(key);
    }

}
