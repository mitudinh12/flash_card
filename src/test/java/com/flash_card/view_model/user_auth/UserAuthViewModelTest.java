package com.flash_card.view_model.user_auth;

import com.flash_card.model.dao.UserDao;
import com.flash_card.model.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserAuthViewModelTest {
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("FlashcardMariaDbUnitTest");
    private static EntityManager entityManager = entityManagerFactory.createEntityManager();
    private UserAuthViewModel userAuthViewModel = UserAuthViewModel.getInstance(entityManager);
    private UserDao userDao = UserDao.getInstance(entityManager);

    @Test
    void getInstance() {
        UserAuthViewModel userAuthViewModel = UserAuthViewModel.getInstance(entityManager);
        assertNotNull(userAuthViewModel);
    }

    @Test
    void openIdConnectWithGoogle() {
        Runnable onSuccess = () -> System.out.println("Success");
        Runnable onFailure = () -> System.out.println("Failure");
        userAuthViewModel.openIdConnectWithGoogle(onSuccess, onFailure);
    }

    @Test
    void persistOrUpdateUser() {
        User user = new User("789", "Jo", "Hands", "jo@gmail.com", "123456");
        Map<String, String> result = Map.of("userId", "789", "firstName", "Jo", "lastName", "Hands", "email", "jo@gmail.com", "idToken", "123456");
        userAuthViewModel.persistOrUpdateUser(result);
        User userFromDb = userDao.findById("789");
        assertNotNull(userFromDb);
        assertEquals(user.getEmail(), userFromDb.getEmail());

        Map<String, String> result2 = Map.of("userId", "789", "firstName", "Jo", "lastName", "Hands", "email", "jo1@gmail.com", "idToken", "123456");
        userAuthViewModel.persistOrUpdateUser(result2);
        User userFromDb2 = userDao.findById("789");
        assertEquals("jo1@gmail.com", userFromDb2.getEmail());

        userDao.delete(userFromDb2);
    }
}