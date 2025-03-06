package com.flash_card.view_model.user_auth;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocalServerTest {

    private LocalServer localServer;

    @BeforeEach
    public void setUp() {
        localServer = new LocalServer();
        assertDoesNotThrow(() -> localServer.stop());
    }

    @Test
    public void testLocalServer() {
        assertNotNull(localServer);
    }

    @Test
    public void testStart() {
        assertDoesNotThrow(() -> localServer.start());
        assertTrue(localServer.isRunning());
    }

    @Test
    public void testStop() {
        if (localServer.getServer() != null) {
            assertDoesNotThrow(() -> localServer.stop());
            assertFalse(localServer.isRunning());
        }
    }

    @Test
    public void testGetAuthorizationCode() {
        assertNull(localServer.getAuthorizationCode());
    }

    @Test
    public void testClearAuthorizationCode() {
        localServer.clearAuthorizationCode();
        assertNull(localServer.getAuthorizationCode());
    }
}