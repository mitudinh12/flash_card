package com.flash_card.view_model.user_auth;

import com.flash_card.localization.Localization;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class LocalServer {
    private HttpServer server;
    private String authorizationCode;
    private boolean isRunning = false;
    private final Localization localization = Localization.getInstance();

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(3000), 0);
        server.createContext("/", exchange -> {
            // Extract the authorization code from the query parameters
            String query = exchange.getRequestURI().getQuery();
            if (query != null && query.contains("code=")) {
                authorizationCode = query.split("code=")[1].split("&")[0];
                String response = localization.getMessage("auth.response");
                exchange.sendResponseHeaders(200, response.length());
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

    public void stop() {
        if (server != null) {
            server.stop(0);
            isRunning = false;
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public HttpServer getServer() {
        return server;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void clearAuthorizationCode() {
        this.authorizationCode = null;
    }
}

