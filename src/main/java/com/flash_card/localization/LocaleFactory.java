package com.flash_card.localization;

import java.util.Locale;

public final class LocaleFactory {
    /**
     * Locale for English (United States).
     */
    private static Locale enLocale;
    /**
     * Locale for Finnish (Finland).
     */
    private static Locale fiLocale;
    /**
     * Locale for Korean (South Korea).
     */
    private static Locale koLocale;
    /**
     * Locale for Thai (Thailand).
     */
    private static Locale thLocale;
    /**
     * Locale for Vietnamese (Vietnam).
     */
    private static Locale viLocale;

    private LocaleFactory() {
        // Private constructor to prevent instantiation
    }
    /**
     * Returns a Locale object based on the provided language code.
     *
     * @param lang The language code (e.g., "en", "fi", "ko", "th", "vi").
     * @return A Locale object corresponding to the specified language code.
     */
    public static Locale getLocaleByLanguage(final String lang) {
        switch (lang) {
            case "fi":
                if (fiLocale == null) {
                    fiLocale = new Locale("fi", "FI");
                }
                return fiLocale;
            case "ko":
                if (koLocale == null) {
                    koLocale = new Locale("ko", "KR");
                }
                return koLocale;
            case "th":
                if (thLocale == null) {
                    thLocale = new Locale("th", "TH");
                }
                return thLocale;
            case "vi":
                if (viLocale == null) {
                    viLocale = new Locale("vi", "VN");
                }
                return viLocale;
            default:
                if (enLocale == null) {
                    enLocale = new Locale("en", "US");
                }
                return enLocale;
        }
    }
}
