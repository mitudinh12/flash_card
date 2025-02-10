package com.flash_card.view_model.user_auth;

import com.flash_card.model.entity.User;

import java.util.Map;

public class AuthSessionViewModel {
    private static AuthSessionViewModel authSessionViewModel = null;
    private Map<String, String> verifiedUserInfo = null;

    private AuthSessionViewModel() {
    }

    public static AuthSessionViewModel getInstance() {
        if (authSessionViewModel == null) {
            authSessionViewModel = new AuthSessionViewModel();
        }
        return authSessionViewModel;
    }

    public void login(Map<String, String> userInfo) {
        this.verifiedUserInfo = userInfo;

    }

    public void logout() {
        this.verifiedUserInfo = null;
    }

    public boolean isAuthenticated() {
        return verifiedUserInfo != null;
    }

    public Map<String, String> getVerifiedUserInfo() {
        return verifiedUserInfo;
    }
}
