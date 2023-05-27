package com.maquetech.application.helpers;

import com.maquetech.application.Application;

public class EnvironmentHelper {

    private EnvironmentHelper() {

    }

    public static String getAdminCode() {
        return getPropertyByCode("root.user.code");
    }

    private static String getPropertyByCode(String code) {
        return Application.ENVIRONMENT_SERVICE.getProperty(code);
    }
}
