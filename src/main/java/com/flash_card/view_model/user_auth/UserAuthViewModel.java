package com.flash_card.view_model.user_auth;

import com.flash_card.model.dao.UserDao;
import com.flash_card.model.entity.User;
import javafx.concurrent.Task;
import com.flash_card.view_model.user_auth.LocalServer;
import com.flash_card.view_model.user_auth.OAuthFlow;
import com.flash_card.view_model.user_auth.TokenDecoder;
import com.flash_card.view_model.user_auth.TokenExchange;

import java.util.Map;

public class UserAuthViewModel {
    private final UserDao userDao = UserDao.getInstance();
    private OAuthFlow oauthFlow = new OAuthFlow();
    private LocalServer localServer = new LocalServer();
    private static UserAuthViewModel userAuthViewModel = null;
    private AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();

    public static UserAuthViewModel getInstance() {
        if (userAuthViewModel == null) {
            userAuthViewModel = new UserAuthViewModel();
        }
        return userAuthViewModel;
    }

    public void openIdConnectWithGoogle(Runnable onSuccess, Runnable onFailure) {
        Task<Void> authTask = new Task<>() {
            @Override
            protected Void call() {
                try {
                    localServer.start();
                    oauthFlow.startOAuth();

                    long startTime = System.currentTimeMillis();
                    long timeout = 15000;

                    while (localServer.getAuthorizationCode() == null) {
                        if (System.currentTimeMillis() - startTime > timeout) {
                            System.out.println("Authentication timed out.");
                            if (onFailure != null) {
                                javafx.application.Platform.runLater(onFailure);
                            }
                            return null;
                        }
                        Thread.sleep(500);
                    }

                    String authorizationCode = localServer.getAuthorizationCode();
                    TokenExchange tokenExchange = new TokenExchange();
                    var tokenResponse = tokenExchange.exchangeCodeForTokens(authorizationCode);
                    String idToken = (String) tokenResponse.get("id_token");
                    var result = TokenDecoder.verifyIDToken(idToken);

                    if (result != null) {
                        persistOrUpdateUser(result);
                        result.remove("idToken");
                        authSessionViewModel.login(result);
                        localServer.clearAuthorizationCode();
                        if (onSuccess != null) {
                            javafx.application.Platform.runLater(onSuccess);
                        }
                    } else {
                        if (onFailure != null) {
                            javafx.application.Platform.runLater(onFailure);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if (onFailure != null) {
                        javafx.application.Platform.runLater(onFailure);
                    }
                } finally {
                    localServer.stop();
                }
                return null;
            }
        };
        new Thread(authTask).start();
    }

    public void persistOrUpdateUser(Map<String, String> userInfo) {
        String userId = userInfo.get("userId");
        String firstName = userInfo.get("firstName");
        String lastname = userInfo.get("lastName");
        String email = userInfo.get("email");
        String idToken = userInfo.get("idToken");
        User user = new User(userId, firstName, lastname, email, idToken);
        if (userDao.findById(userId) == null) {
            userDao.persist(user);
        } else {
            userDao.update(user);
        }
    }
}
