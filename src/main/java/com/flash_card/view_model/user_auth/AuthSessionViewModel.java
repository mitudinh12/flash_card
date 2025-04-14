package com.flash_card.view_model.user_auth;

import java.util.Map;

/**
 * Singleton ViewModel that manages the authenticated user's session state.
 * Stores and provides access to verified user information after login.
 */
public final class AuthSessionViewModel {

    /**
     * The singleton instance of {@code AuthSessionViewModel}.
     */
    private static AuthSessionViewModel authSessionViewModel = null;

    /**
     * A map containing verified user information such as user ID, name, and email.
     * If {@code null}, no user is currently logged in.
     */
    private Map<String, String> verifiedUserInfo = null;

    /**
     * Private constructor to prevent instantiation from outside the class.
     */
    private AuthSessionViewModel() {
    }

    /**
     * Returns the singleton instance of {@code AuthSessionViewModel}.
     *
     * @return the single instance of the session ViewModel
     */
    public static AuthSessionViewModel getInstance() {
        if (authSessionViewModel == null) {
            authSessionViewModel = new AuthSessionViewModel();
        }
        return authSessionViewModel;
    }

    /**
     * Stores the verified user information in the session.
     *
     * @param userInfo a map containing user details such as ID, email, and names
     */
    public void login(final Map<String, String> userInfo) {
        this.verifiedUserInfo = userInfo;
    }

    /**
     * Clears the session and removes all stored user information.
     */
    public void logout() {
        this.verifiedUserInfo = null;
    }

    /**
     * Checks whether a user is currently authenticated.
     *
     * @return {@code true} if the user is logged in; {@code false} otherwise
     */
    public boolean isAuthenticated() {
        return verifiedUserInfo != null;
    }

    /**
     * Returns the map containing verified user information.
     *
     * @return a map of user details, or {@code null} if no user is logged in
     */
    public Map<String, String> getVerifiedUserInfo() {
        return verifiedUserInfo;
    }
}
