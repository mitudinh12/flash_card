package com.flash_card.view_model.user_auth;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Handles the OAuth 2.0 authorization flow by opening the user's default browser
 * to initiate the login process with Google.
 */
public class OAuthFlow {

    /**
     * The client ID registered with Google's OAuth 2.0 server.
     */
    private static final String CLIENT_ID = "681362255483-cuu4bkd2c3hlqtdgsjdas0slohpvbpb8.apps.googleusercontent.com";

    /**
     * The redirect URI that matches the one configured in the Google API Console.
     */
    private static final String REDIRECT_URI = "http://localhost:3000";

    /**
     * The full authorization URL to initiate the OAuth 2.0 flow.
     */
    private static final String AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth"
            + "?client_id=" + CLIENT_ID
            + "&redirect_uri=" + REDIRECT_URI
            + "&response_type=code"
            + "&scope=openid%20email%20profile";

    /**
     * Starts the OAuth authorization flow by opening the system's default browser.
     * If the system does not support opening a browser, the authorization URL is printed.
     *
     * @throws IOException if the desktop operation fails
     * @throws URISyntaxException if the authorization URL is malformed
     */
    public void startOAuth() throws IOException, URISyntaxException {
        if (isBrowserSupported()) {
            Desktop.getDesktop().browse(new URI(AUTH_URL));
        } else {
            System.out.println("Opening a browser is not supported on this platform.");
            System.out.println("Please open the following URL manually:");
            System.out.println(AUTH_URL);
        }
    }

    /**
     * Checks whether the platform supports opening a browser using the {@link Desktop} class.
     *
     * @return {@code true} if the platform supports browsing, {@code false} otherwise
     */
    private boolean isBrowserSupported() {
        return Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE);
    }
}