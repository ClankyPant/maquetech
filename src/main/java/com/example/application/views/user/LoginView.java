package com.example.application.views.user;

import com.vaadin.flow.component.button.Button;
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

        loginForm.setAction("login");
        loginForm.setI18n(initLoginI18n());
        loginForm.setForgotPasswordButtonVisible(false);

        add(loginForm, new RouterLink("Cadastrar-se", RegistrationView.class));
    }

    private LoginI18n initLoginI18n() {
        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setForm(getFormMessageConfiguration(i18n));
        i18n.setErrorMessage(getFormErrorMessageConfiguration(i18n));
        return i18n;
    }

    private LoginI18n.ErrorMessage getFormErrorMessageConfiguration(LoginI18n i18n) {
        LoginI18n.ErrorMessage result = i18n.getErrorMessage();
        result.setTitle("Erro ao logar");
        result.setMessage("Usuario/Senha inválidos!");

        return result;
    }

    private LoginI18n.Form getFormMessageConfiguration(LoginI18n i18n) {
        LoginI18n.Form result = i18n.getForm();
        result.setTitle("MaqueTech");
        result.setUsername("Usuário");
        result.setPassword("Senha");
        result.setSubmit("Login");
        result.setForgotPassword("Esqueci minha senha");

        return result;
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
