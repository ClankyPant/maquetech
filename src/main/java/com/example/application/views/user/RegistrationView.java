package com.example.application.views.user;

import com.example.application.entities.user.UserEntity;
import com.example.application.enums.user.UserTypeEnum;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.userdetails.User;

import java.util.Arrays;

@AnonymousAllowed
@PageTitle("Cadastro de usuário")
@Route(value = "registration")
public class RegistrationView extends VerticalLayout {

    public RegistrationView() {
        configLayout();

        VerticalLayout vlContent = new VerticalLayout();
        vlContent.setWidth("50%");

        Binder<UserEntity> binder = new Binder<>(UserEntity.class);

        FormLayout formLayout = new FormLayout();

        TextField usernameField = new TextField("Login");
        usernameField.setRequired(true);
        binder.bind(usernameField, UserEntity::getUsername, UserEntity::setUsername);

        PasswordField passwordField = new PasswordField("Senha");
        passwordField.setRequired(true);
        binder.bind(passwordField, UserEntity::getPassword, UserEntity::setPassword);

        EmailField mailField = new EmailField("E-mail");
        mailField.setRequired(true);
        binder.bind(mailField, UserEntity::getMail, UserEntity::setMail);

        TextField cpfField = new TextField("CPF");
        cpfField.setAllowedCharPattern("[0-9]");
        cpfField.setRequired(true);
        binder.bind(cpfField, UserEntity::getCpf, UserEntity::setCpf);

        TextField phoneField = new TextField("Telefone");
        phoneField.setPattern("^[+]?[(]?[0-9]{3}[)]?[-s.]?[0-9]{3}[-s.]?[0-9]{4,6}$");
        phoneField.setAllowedCharPattern("[0-9]");
        phoneField.setMinLength(10);
        phoneField.setMaxLength(11);
        phoneField.setRequired(true);
        binder.bind(phoneField, UserEntity::getPhone, UserEntity::setPhone);

        ComboBox<UserTypeEnum> typeField = new ComboBox<>("Tipo usuário");
        typeField.setItems(Arrays.asList(UserTypeEnum.NORMAL, UserTypeEnum.PROFESSOR));
        typeField.setItemLabelGenerator(UserTypeEnum::getName);

        formLayout.add(usernameField, passwordField, mailField, cpfField, phoneField, typeField);
        formLayout.setColspan(mailField, 2);

        Button btnRegister = new Button("Cadastrar-se");
        btnRegister.addClickListener((event) -> {
            try {
                UserEntity userEntity = new UserEntity();
                binder.writeBean(userEntity);


            } catch (ValidationException ex) {
                Notification notification = new Notification("Alguns campos não foram preenchidos corretamente!");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.open();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        vlContent.add(formLayout, btnRegister);

        add(vlContent);
    }

    private void configLayout() {
        setSizeFull();
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    }
}
