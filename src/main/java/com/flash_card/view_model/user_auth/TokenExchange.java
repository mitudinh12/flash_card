package com.flash_card.view_model.user_auth;

import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import java.io.IOException;

/**
 * Handles the exchange of an authorization code for access and ID tokens
 * using Google's OAuth 2.0 token endpoint.
 */
public class TokenExchange {
    /**
     * The client ID issued by Google's OAuth 2.0 server.
     */
    private static final String CLIENT_ID = "681362255483-cuu4bkd2c3hlqtdgsjdas0slohpvbpb8.apps.googleusercontent.com";

    /**
     * The client secret associated with the client ID.
     */
    private static final String CLIENT_SECRET = "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";

    /**
     * The URL of Google's token endpoint.
     */
    private static final String TOKEN_SERVER_URL = "https://oauth2.googleapis.com/token";

    /**
     * The redirect URI that matches the one used during authorization.
     */
    private static final String REDIRECT_URI = "http://localhost:3000";

    /**
     * Exchanges the given authorization code for access and ID tokens
     * by making a request to Google's OAuth 2.0 token endpoint.
     *
     * @param authorizationCode the authorization code received from the authorization server
     * @return a {@link TokenResponse} object containing the access token, ID token, and other data
     * @throws IOException if the token exchange request fails
     */
    public TokenResponse exchangeCodeForTokens(final String authorizationCode) throws IOException {
        // Exchange the authorization code for tokens
        return new AuthorizationCodeTokenRequest(
                new NetHttpTransport(),
                new GsonFactory(),
                new GenericUrl(TOKEN_SERVER_URL),
                authorizationCode)
                .setRedirectUri(REDIRECT_URI)
                .setClientAuthentication(new BasicAuthentication(CLIENT_ID, CLIENT_SECRET))
                .execute();

    }
}

