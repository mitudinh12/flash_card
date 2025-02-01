package com.flash_card.view.auth;

import com.flash_card.view_model.user_auth.LocalServer;
import com.flash_card.view_model.user_auth.OAuthFlow;
import com.flash_card.view_model.user_auth.TokenDecoder;
import com.flash_card.view_model.user_auth.TokenExchange;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.Map;

public class LoginViewController  {
    OAuthFlow oauthFlow = new OAuthFlow();
    LocalServer localServer = new LocalServer();

    @FXML
    private Button loginButton;

    @FXML
    private void loginWithGoogle() {
        try {
            // Start the local server to capture the authorization code
            localServer.start();

            // Open the browser for user authentication
            oauthFlow.startOAuth();

            // Wait for the authorization code
            while (localServer.getAuthorizationCode() == null) {
                Thread.sleep(1000);
            }

            String authorizationCode = localServer.getAuthorizationCode();
            System.out.println("Authorization Code: " + authorizationCode);

            // Exchange the code for tokens
            TokenExchange tokenExchange = new TokenExchange();
            var tokenResponse = tokenExchange.exchangeCodeForTokens(authorizationCode);
            String idToken = (String) tokenResponse.get("id_token");
            var result = TokenDecoder.verifyIDToken(idToken);
            if (result != null) {
                Map<String, String> userInfo = result;

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            localServer.stop();
        }
    }
}
