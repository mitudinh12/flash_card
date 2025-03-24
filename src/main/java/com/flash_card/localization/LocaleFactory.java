package com.flash_card.localization;

import java.util.Locale;

public class LocaleFactory {
    private static Locale enLocale;
    private static Locale fiLocale;
    private static Locale koLocale;
    private static Locale thLocale;
    private static Locale viLocale;

    public static Locale getLocaleByLanguage(String lang) {
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
