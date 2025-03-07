package com.flash_card.view_model.user_auth;

import static org.junit.jupiter.api.Assertions.*;

import com.flash_card.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


class AuthSessionViewModelTest {
    AuthSessionViewModel authSessionViewModel;
    Map<String, String> testUser1Info = new HashMap<>();
    User testUser1 = new User("authid", "auth", "session", "auth@gmail,com", "authtoken");

    @BeforeEach
    public void setUp() {
        authSessionViewModel = AuthSessionViewModel.getInstance();
        testUser1Info.put("firstName", testUser1.getFirstName());
        testUser1Info.put("lastName", testUser1.getLastName());
        testUser1Info.put("email", testUser1.getEmail());
        testUser1Info.put("userId", testUser1.getUserId());
    }

    @Test
    public void testAuthSessionViewModel() {
        assertNotNull(authSessionViewModel);
    }

    @Test
    public void testLogin() {
        authSessionViewModel.login(testUser1Info);
        assertNotNull(authSessionViewModel.getVerifiedUserInfo());
        assertEquals(testUser1Info, authSessionViewModel.getVerifiedUserInfo());
    }

    @Test
    public void testLogout() {
        authSessionViewModel.login(testUser1Info);
        assertNotNull(authSessionViewModel.getVerifiedUserInfo());
        authSessionViewModel.logout();
        assertNull(authSessionViewModel.getVerifiedUserInfo());
    }

    @Test
    public void testIsAuthenticated() {
        authSessionViewModel.logout();
        assertFalse(authSessionViewModel.isAuthenticated());
        authSessionViewModel.login(testUser1Info);
        assertTrue(authSessionViewModel.isAuthenticated());
    }

    @Test
    public void testGetVerifiedUserInfo() {
        authSessionViewModel.login(testUser1Info);
        assertEquals(testUser1Info, authSessionViewModel.getVerifiedUserInfo());
    }

    @Test
    public void testGetInstance() {
        AuthSessionViewModel authSessionViewModel2 = AuthSessionViewModel.getInstance();
        assertEquals(authSessionViewModel, authSessionViewModel2);
    }


}