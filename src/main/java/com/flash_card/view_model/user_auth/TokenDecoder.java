package com.flash_card.view_model.user_auth;

import com.google.api.client.auth.openidconnect.IdToken;
import com.google.api.client.auth.openidconnect.IdTokenVerifier;
import com.google.api.client.json.gson.GsonFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class responsible for decoding and verifying OpenID Connect ID tokens
 * obtained from Google's OAuth 2.0 authentication service.
 */
public final class TokenDecoder {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private TokenDecoder() {
        // Prevent instantiation
    }
    /**
     * The client ID issued by Google's OAuth 2.0 server.
     */
    private static final String CLIENT_ID = "681362255483-cuu4bkd2c3hlqtdgsjdas0slohpvbpb8.apps.googleusercontent.com";

    /**
     * Verifies the given ID token using the configured audience and issuer,
     * and extracts user information if the token is valid.
     *
     * @param token the ID token to verify
     * @return a map containing user details such as userId, email, firstName, lastName, and idToken;
     *         or {@code null} if the token is invalid
     * @throws Exception if token parsing or verification fails
     */
    public static Map<String, String> verifyIDToken(final String token) throws Exception {

        IdTokenVerifier verifier = new IdTokenVerifier.Builder()
                .setAudience(Collections.singletonList(CLIENT_ID))
                .setIssuer("https://oauth2.googleapis.com/token")
                .build();
        IdToken idToken = IdToken.parse(new GsonFactory(), token);
        if (!verifier.verify(idToken)) {
            IdToken.Payload payload = idToken.getPayload();

            // User information
            String userId = payload.getSubject();
            String email = (String) payload.get("email");
            String lastName = (String) payload.get("family_name");
            String firstName = (String) payload.get("given_name");

            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("userId", userId);
            userInfo.put("email", email);
            userInfo.put("firstName", firstName);
            userInfo.put("lastName", lastName);
            userInfo.put("idToken", token);
            return userInfo;
        } else {
            System.out.println("Invalid ID token.");
        }
        return null;
    }
}
