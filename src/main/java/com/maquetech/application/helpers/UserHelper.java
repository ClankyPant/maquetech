package com.maquetech.application.helpers;

import com.maquetech.application.Application;
import com.maquetech.application.entities.user.UserEntity;
import javassist.NotFoundException;

public class UserHelper {

    public static UserEntity getLoggedUser() throws NotFoundException {
        return Application.USER_SERVICE.getLoggedUser();
    }

    public static void logout() {
        Application.AUTHENTICATION_CONTEXT.logout();
    }
}
