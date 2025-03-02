package com.flash_card.view_model.user_auth;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TokenDecoderTest {

    @Test
    void verifyIDToken() {
        String token = "dasder5435sdfds";
        assertThrows(Exception.class, () -> TokenDecoder.verifyIDToken(token));

        String token2 = "eyJhbGciOiJSUzI1NiIsImtpZCI6Ijc2M2Y3YzRjZDI2YTFlYjJiMWIzOWE4OGY0NDM0ZDFmNGQ5YTM2OGIiLCJ0eXAiOiJKV1QifQ";
        try {
            Map<String, String> userInfo = TokenDecoder.verifyIDToken(token2);
            if (userInfo != null) {
                assertEquals(userInfo.get("email"), "thaonhi6991@gmail.com");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}