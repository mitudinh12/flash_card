package com.flash_card.view_model.user_auth;

import com.flash_card.localization.Localization;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

/**
 * A simple local HTTP server that listens for the OAuth 2.0 redirect request
 * and extracts the authorization code from the query parameters.
 */
public class LocalServer {

    /**
     * The port on which the local server will listen for requests.
     */
    private static final int SERVER_PORT = 3000;

    /**
     * The HTTP status code sent upon successful authorization code receipt.
     */
    private static final int HTTP_OK = 200;

    /**
     * The embedded HTTP server used to listen for incoming requests.
     */
    private HttpServer server;

    /**
     * The extracted authorization code from the redirect request.
     */
    private String authorizationCode;

    /**
     * Indicates whether the server is currently running.
     */
    private boolean isRunning = false;

    /**
     * Provides localized response messages for the authorization flow.
     */
    private final Localization localization = Localization.getInstance();

    /**
     * Starts the local HTTP server on port 3000 and listens for OAuth 2.0 redirect requests.
     * When a request with an authorization code is received, the code is extracted,
     * a response is sent to the browser, and the server stops itself.
     *
     * @throws IOException if the server fails to start or bind to the port
     */
    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(SERVER_PORT), 0);
        server.createContext("/", exchange -> {
            // Extract the authorization code from the query parameters
            String query = exchange.getRequestURI().getQuery();
            if (query != null && query.contains("code=")) {
                authorizationCode = query.split("code=")[1].split("&")[0];
                String response = localization.getMessage("auth.response");
                exchange.sendResponseHeaders(HTTP_OK, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();

                // Stop the server after receiving the code
                stop();
            }
        });
        server.start();
        isRunning = true;
    }

    /**
     * Stops the local HTTP server if it is running.
     */
    public void stop() {
        if (server != null) {
            server.stop(0);
            isRunning = false;
        }
    }

    /**
     * Returns whether the local server is currently running.
     *
     * @return {@code true} if the server is running; {@code false} otherwise
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Returns the underlying {@link HttpServer} instance.
     *
     * @return the internal {@code HttpServer} instance
     */
    public HttpServer getServer() {
        return server;
    }

    /**
     * Returns the authorization code received from the OAuth 2.0 redirect.
     *
     * @return the authorization code, or {@code null} if none has been received
     */
    public String getAuthorizationCode() {
        return authorizationCode;
    }

    /**
     * Clears the stored authorization code.
     */
    public void clearAuthorizationCode() {
        this.authorizationCode = null;
    }
}
