package com.example.application.views.user;

import com.example.application.entities.user.UserEntity;
import com.example.application.enums.user.UserTypeEnum;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
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
        binder.forField(usernameField)
                .withValidator(username -> username.length() > 5, "Login deve conter mais que 5 caracteres!")
                .bind(UserEntity::getUsername, UserEntity::setUsername);

        PasswordField passwordField = new PasswordField("Senha");
        passwordField.setRequired(true);
        passwordField.setAllowedCharPattern("[A-Za-z0-9]");
        passwordField.setHelperText("Apenas letras e numeros são permitidos.");
        binder.forField(passwordField)
                .withValidator(password -> password.length() >= 6, "Senha precisa ter no minimo 6 caracteres")
                .withValidator(password -> password.length() <= 12, "Senha precisa ter no máximo 12 caracteres")
                .asRequired()
                .bind(UserEntity::getPassword, UserEntity::setPassword);

        EmailField mailField = new EmailField("E-mail");
        mailField.setRequired(true);
        binder.forField(mailField)
                .asRequired()
                .bind(UserEntity::getMail, UserEntity::setMail);

        TextField cpfField = new TextField("CPF");
        cpfField.setAllowedCharPattern("[0-9]");
        cpfField.setRequired(true);
        binder.forField(cpfField)
                .asRequired()
                .bind(UserEntity::getCpf, UserEntity::setCpf);

        TextField phoneField = new TextField("Telefone");
        phoneField.setPattern("^[+]?[(]?[0-9]{3}[)]?[-s.]?[0-9]{3}[-s.]?[0-9]{4,6}$");
        phoneField.setAllowedCharPattern("[0-9]");
        phoneField.setMinLength(10);
        phoneField.setMaxLength(11);
        phoneField.setRequired(true);
        binder.forField(phoneField)
                .asRequired()
                .bind(UserEntity::getPhone, UserEntity::setPhone);

        ComboBox<UserTypeEnum> typeField = new ComboBox<>("Tipo usuário");
        typeField.setItems(Arrays.asList(UserTypeEnum.NIVEL_1, UserTypeEnum.NIVEL_2));
        typeField.setItemLabelGenerator(UserTypeEnum::getDescription);
        typeField.setValue(UserTypeEnum.NIVEL_1);
        binder.forField(typeField)
                .asRequired()
                .bind(UserEntity::getType, UserEntity::setType);

        formLayout.add(usernameField, passwordField, mailField, cpfField, phoneField, typeField);
        formLayout.setColspan(mailField, 2);

        Button btnRegister = new Button("Cadastrar-se");
        btnRegister.addClickListener((event) -> {
            try {
                UserEntity userEntity = new UserEntity();
                binder.writeBean(userEntity);
                

            } catch (ValidationException ex) {
                notifyErro("Alguns campos não foram preenchidos corretamente!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        vlContent.add(formLayout, btnRegister);

        add(vlContent);
    }

    private void notifyErro(String message) {
        Notification notification = new Notification(message);
        notification.setDuration(3000);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.open();
    }

    private void configLayout() {
        setSizeFull();
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    }
}
