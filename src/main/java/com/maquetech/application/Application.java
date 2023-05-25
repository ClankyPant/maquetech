package com.maquetech.application;

import com.maquetech.application.services.user.UserService;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@NpmPackage(value = "@fontsource/poppins", version = "4.5.0")
@Theme(value = "maquetech", variant = Lumo.DARK)
public class Application implements AppShellConfigurator {

    public static UserService USER_SERVICE;
    public static AuthenticationContext AUTHENTICATION_CONTEXT;

    public Application(UserService userService, AuthenticationContext authenticationContext) {
        Application.USER_SERVICE = userService;
        Application.AUTHENTICATION_CONTEXT = authenticationContext;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
