package com.flash_card;

import com.flash_card.view.auth.LoginView;

/**
 * The entry point of the Flash Card application.
 * <p>
 * This class launches the JavaFX application starting from the LoginView.
 */
public final class Main {
    private Main() { }
    /**
     * Main method which starts the JavaFX application.
     *
     * @param args the command-line arguments passed to the program
     */
    public static void main(final String[] args) {
        LoginView.launch(LoginView.class);
    }
}
