package com.example.application.views.usuario;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@AnonymousAllowed
@PageTitle("Login")
@Route(value = "login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm loginForm = new LoginForm();

    public LoginView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setForm(getConfiguracaoLogin(i18n));

        loginForm.setAction("login");
        loginForm.setI18n(i18n);

        add(loginForm);
    }

    private LoginI18n.Form getConfiguracaoLogin(LoginI18n i18n) {
        LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setTitle("MaqueTech");
        i18nForm.setUsername("Usuário");
        i18nForm.setPassword("Senha");
        i18nForm.setSubmit("Login");
        i18nForm.setForgotPassword("Esqueci minha senha");

        return i18nForm;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // inform the user about an authentication error
        if(beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            loginForm.setError(true);
        }
    }
}
