package com.flash_card.framework;
/**
 * Enum representing different languages available in the flash card application.
 */
public enum Language {
    /**
     * English language.
     */
    english("English"),
    /**
     * Finnish language.
     */
    finnish("Suomi"),
    /**
     * Vietnamese language.
     */
    vietnamese("Tiếng Việt"),
    /**
     * Korean language.
     */
    korean("한국인"),
    /**
     * Thai language.
     */
    thai("ภาษาไทย");
    /**
     * Display name of the language.
     */
    private final String displayName;

    /**
     * Constructor for the Language enum.
     *
     * @param displayNameParam the display name of the language
     */

    Language(final String displayNameParam) {
        this.displayName = displayNameParam;
    }
    /**
     * Returns the Language enum constant corresponding to the given display name.
     *
     * @param displayName the display name of the language
     * @return the Language enum constant
     * @throws IllegalArgumentException if the display name does not match any language
     */
    public static Language fromDisplayName(final String displayName) {
        for (Language language : Language.values()) {
            if (language.getDisplayName().equalsIgnoreCase(displayName)) {
                return language;
            }
        }
        throw new IllegalArgumentException("Unknown language: " + displayName);
    }

    /**
     * Returns the display name of the language.
     *
     * @return the display name of the language
     */
    public String getDisplayName() {
        return displayName;
    }
}
