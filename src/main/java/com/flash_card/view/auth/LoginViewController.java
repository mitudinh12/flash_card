package com.flash_card.view.auth;

import com.flash_card.view_model.user.UserInfoViewModel;
import com.flash_card.view_model.user_auth.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.Map;

public class LoginViewController  {
    OAuthFlow oauthFlow = new OAuthFlow();
    LocalServer localServer = new LocalServer();
    UserAuthViewModel userAuthViewModel = UserAuthViewModel.getInstance();

    @FXML
    private Button loginButton;

    @FXML
    private void loginWithGoogle() {
        userAuthViewModel.openIdConnectWithGoogle();
    }
}
