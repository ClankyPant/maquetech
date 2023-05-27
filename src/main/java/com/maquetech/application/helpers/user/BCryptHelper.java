package com.maquetech.application.helpers.user;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptHelper {

    public static String enconde(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
