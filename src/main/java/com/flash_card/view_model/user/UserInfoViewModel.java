package com.flash_card.view_model.user;

import com.flash_card.model.dao.UserDao;
import com.flash_card.model.entity.User;

import java.util.Map;

public class UserInfoViewModel {
    private final UserDao userDao = new UserDao();
    private static UserInfoViewModel userInfoViewModel = null;
    public UserInfoViewModel() {

    }

    public static UserInfoViewModel getInstance() {
        if (userInfoViewModel == null) {
            userInfoViewModel = new UserInfoViewModel();
        }
        return userInfoViewModel;
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
