package com.flash_card.view_model.user_auth;

import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import java.io.IOException;

public class TokenExchange {
    private static final String CLIENT_ID = "681362255483-cuu4bkd2c3hlqtdgsjdas0slohpvbpb8.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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";
    private static final String TOKEN_SERVER_URL = "https://oauth2.googleapis.com/token";
    private static final String REDIRECT_URI = "http://localhost:3000";

    public TokenResponse exchangeCodeForTokens(String authorizationCode) throws IOException {
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

