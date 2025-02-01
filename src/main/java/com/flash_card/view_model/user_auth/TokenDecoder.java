package com.flash_card.view_model.user_auth;

import com.google.api.client.auth.openidconnect.IdToken;
import com.google.api.client.auth.openidconnect.IdTokenVerifier;
import com.google.api.client.json.gson.GsonFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TokenDecoder {
    private static final String CLIENT_ID = "681362255483-cuu4bkd2c3hlqtdgsjdas0slohpvbpb8.apps.googleusercontent.com";

    public static Map<String, String> verifyIDToken(String token) throws Exception {

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
            String name = (String) payload.get("name");
            String lastName = (String) payload.get("family_name");
            String firstName = (String) payload.get("given_name");

            // Display user information
            System.out.println(payload);
            System.out.println("User ID: " + userId);
            System.out.println("Email: " + email);
            System.out.println("Name: " + name);
            System.out.println("Family Name: " + lastName);
            System.out.println("Given Name: " + firstName);

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
