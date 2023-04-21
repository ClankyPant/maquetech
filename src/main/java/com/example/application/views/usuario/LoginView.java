package com.example.application.views.usuario;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@PageTitle("Login")
@Route(value = "login")
@AnonymousAllowed
public class LoginView extends VerticalLayout {

    public LoginView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setForm(getConfiguracaoLogin(i18n));
        LoginForm loginForm = new LoginForm();
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
}
