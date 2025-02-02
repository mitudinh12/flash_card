package com.flash_card.view_model.user;

import com.flash_card.model.dao.UserDao;
import com.flash_card.model.entity.User;

import java.util.Map;

public class UserInfoViewModel {
    private final UserDao userDao = UserDao.getInstance();
    private static UserInfoViewModel userInfoViewModel = null;
    public UserInfoViewModel() {

    }

    public static UserInfoViewModel getInstance() {
        if (userInfoViewModel == null) {
            userInfoViewModel = new UserInfoViewModel();
        }
        return userInfoViewModel;
    }


}
