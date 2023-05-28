package com.maquetech.application.components.user;

import com.maquetech.application.helpers.NotificationHelper;
import com.maquetech.application.helpers.user.BCryptHelper;
import com.maquetech.application.models.user.UserModel;
import com.maquetech.application.services.user.UserService;
import com.nimbusds.jose.jca.JCAAware;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

public class UserChangePasswordComponent extends Dialog {

    private Long id;
    private final UserService userService;
    private final Binder<UserModel> binder = new Binder<>();
    private final InMemoryUserDetailsManager inMemoryUserDetailsManager;

    public UserChangePasswordComponent(UserService userService, InMemoryUserDetailsManager inMemoryUserDetailsManager) {
        this.userService = userService;
        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;

        init();
    }

    public void init() {
        var newPassword = new PasswordField("Senha");
        newPassword.setRequired(true);
        newPassword.setAllowedCharPattern("[A-Za-z0-9]");
        newPassword.setHelperText("Apenas letras e numeros são permitidos.");
        binder.forField(newPassword)
                .withValidator(input -> input.length() >= 6, "Senha precisa ter no minimo 6 caracteres")
                .withValidator(input -> input.length() <= 12, "Senha precisa ter no máximo 12 caracteres")
                .asRequired()
                .bind(UserModel::getPassword, UserModel::setPassword);

        var cancel = new Button("Cancelar", event -> this.close());
        cancel.addThemeVariants(ButtonVariant.LUMO_ERROR);

        var save = new Button("Salvar");
        save.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        save.addClickListener(event -> {
            try {
                if (binder.isValid()) {
                    var userModel = new UserModel();
                    binder.writeBean(userModel);
                    this.userService.changePassword(this.id, BCryptHelper.enconde(userModel.getPassword()), inMemoryUserDetailsManager);
                    NotificationHelper.success("Senha alterada com sucesso!");
                    this.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                NotificationHelper.error(ex.getMessage());
            }
        });

        add(newPassword);
        getFooter().add(cancel, save);
    }

    public void open(Long id) {
        this.binder.refreshFields();
        this.id = id;
        super.open();
    }
}
