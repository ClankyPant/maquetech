package com.maquetech.application.views.user;

import com.maquetech.application.components.user.UserRegistrationComponent;
import com.maquetech.application.services.course.CourseService;
import com.maquetech.application.services.user.UserService;
import com.maquetech.application.services.user.professor.ProfessorCodeService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.core.env.Environment;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@AnonymousAllowed
@PageTitle("Login")
@Route(value = "login")
public class UserLoginView extends VerticalLayout implements BeforeEnterObserver {

    private final UserService userService;
    private final CourseService courseService;
    private final LoginForm loginForm = new LoginForm();
    private final ProfessorCodeService professorCodeService;
    private final UserRegistrationComponent userRegistrationComponent;
    private final InMemoryUserDetailsManager inMemoryUserDetailsManager;

    public UserLoginView(UserService userService, CourseService courseService, ProfessorCodeService professorCodeService, InMemoryUserDetailsManager inMemoryUserDetailsManager) {
        this.userService = userService;
        this.courseService = courseService;
        this.professorCodeService = professorCodeService;
        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
        this.userRegistrationComponent = new UserRegistrationComponent(this.userService, this.courseService, this.professorCodeService, this.inMemoryUserDetailsManager);

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        loginForm.setAction("login");
        loginForm.setI18n(initLoginI18n());
        loginForm.setForgotPasswordButtonVisible(false);

        add(loginForm, new Button("Cadastrar-se", event -> userRegistrationComponent.open()), userRegistrationComponent);
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
