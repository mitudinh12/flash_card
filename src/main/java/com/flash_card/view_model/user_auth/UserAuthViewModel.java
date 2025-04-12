package com.flash_card.view_model.user_auth;

import com.flash_card.model.dao.UserDao;
import com.flash_card.model.entity.User;
import jakarta.persistence.EntityManager;
import javafx.concurrent.Task;
import java.util.Map;

/**
 * ViewModel responsible for managing user authentication logic,
 * specifically handling Google OpenID Connect login flow and managing authenticated session state.
 */
public class UserAuthViewModel {

    /**
     * Data Access Object for User entity operations.
     */
    private static UserDao userDao;

    /**
     * Handles the OAuth2 authorization flow with Google.
     */
    private final OAuthFlow oauthFlow = new OAuthFlow();

    /**
     * Local server to capture the authorization code returned from Google's OAuth flow.
     */
    private final LocalServer localServer = new LocalServer();

    /**
     * Singleton instance of this ViewModel.
     */
    private static UserAuthViewModel userAuthViewModel = null;

    /**
     * Reference to the session ViewModel for managing login session state.
     */
    private final AuthSessionViewModel authSessionViewModel = AuthSessionViewModel.getInstance();

    /**
     * The maximum amount of time (in milliseconds) to wait for
     * the authentication process to complete before timing out.
     */
    private static final int AUTH_TIMEOUT_MS = 15000;
    /**
     * The interval (in milliseconds) between polling attempts while waiting
     * for the authorization code from the local server.
     */
    private static final int POLLING_INTERVAL_MS = 500;

    /**
     * Returns a singleton instance of the {@code UserAuthViewModel}, initializing the DAO if necessary.
     *
     * @param entityManager the {@code EntityManager} used to initialize the {@code UserDao}
     * @return a singleton instance of {@code UserAuthViewModel}
     */
    public static UserAuthViewModel getInstance(final EntityManager entityManager) {
        if (userAuthViewModel == null) {
            userAuthViewModel = new UserAuthViewModel();
            UserAuthViewModel.userDao = UserDao.getInstance(entityManager);
        }
        return userAuthViewModel;
    }

    /**
     * Initiates the OpenID Connect login flow with Google. On success, user information is persisted or updated.
     * in the database and session state is updated. On failure, the failure callback is executed.
     *
     * @param onSuccess a {@code Runnable} to be executed on successful authentication
     * @param onFailure a {@code Runnable} to be executed if authentication fails or times out
     */
    public void openIdConnectWithGoogle(final Runnable onSuccess, final Runnable onFailure) {
        Task<Void> authTask = new Task<>() {
            @Override
            protected Void call() {
                try {
                    localServer.start();
                    oauthFlow.startOAuth();

                    long startTime = System.currentTimeMillis();
                    long timeout = AUTH_TIMEOUT_MS;

                    while (localServer.getAuthorizationCode() == null) {
                        if (System.currentTimeMillis() - startTime > timeout) {
                            System.out.println("Authentication timed out.");
                            if (onFailure != null) {
                                javafx.application.Platform.runLater(onFailure);
                            }
                            return null;
                        }
                        Thread.sleep(POLLING_INTERVAL_MS);
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

    /**
     * Persists a new user or updates an existing user in the database based on the provided user information.
     *
     * @param userInfo a {@code Map} containing user data such as userId, firstName, lastName, email, and idToken
     */
    public void persistOrUpdateUser(final Map<String, String> userInfo) {
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
