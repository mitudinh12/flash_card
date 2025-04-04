package com.flash_card.framework;

public enum Language {
    ENGLISH("English"),
    FINNISH("Suomi"),
    VIETNAMESE("Tiếng Việt"),
    KOREAN("한국인"),
    THAI("ภาษาไทย");

    private final String displayName;

    Language(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Language fromDisplayName(String displayName) {
        for (Language language : Language.values()) {
            if (language.getDisplayName().equalsIgnoreCase(displayName)) {
                return language;
            }
        }
        throw new IllegalArgumentException("Unknown language: " + displayName);
    }
}