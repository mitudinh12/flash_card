package com.flash_card.view_model.user_auth;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class OAuthFlow {
    private static final String CLIENT_ID = "681362255483-cuu4bkd2c3hlqtdgsjdas0slohpvbpb8.apps.googleusercontent.com";
    private static final String REDIRECT_URI = "http://localhost:3000";
    private static final String AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth"
            + "?client_id=" + CLIENT_ID
            + "&redirect_uri=" + REDIRECT_URI
            + "&response_type=code"
            + "&scope=openid%20email%20profile";

    public void startOAuth() throws IOException, URISyntaxException {
        // Open the default browser to start the authentication flow
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(new URI(AUTH_URL));
        } else {
            System.out.println("Please open the following URL in your browser:");
            System.out.println(AUTH_URL);
        }
    }
}