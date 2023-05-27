package com.maquetech.application.components.user;

import com.maquetech.application.helpers.NotificationHelper;
import com.maquetech.application.helpers.user.UserHelper;
import com.maquetech.application.listener.UserEditedListener;
import com.maquetech.application.models.user.UserModel;
import com.maquetech.application.services.user.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

import java.util.ArrayList;
import java.util.List;

public class UserEditComponent extends Dialog {

    private UserModel user;
    private Binder<UserModel> binder;
    private final UserService userService;
    private final VerticalLayout vlContent = new VerticalLayout();
    private List<UserEditedListener> userEditedListenerList = new ArrayList<>();

    public UserEditComponent(UserService userService) {
        this.userService = userService;

        init();
    }

    private void init() {
        setSizeFull();

        vlContent.setSizeFull();
        vlContent.setAlignItems(FlexComponent.Alignment.CENTER);

        add(vlContent);
    }

    private void initFields() {
        binder = new Binder<>();

        var name = new TextField("Nome");
        name.setRequired(true);
        binder.forField(name)
                .withValidator(input -> input.length() > 4, "Nome deve conter mais que 5 caracteres!")
                .bind(UserModel::getName, UserModel::setName);

        var mail = new EmailField("E-mail");
        mail.setRequired(true);
        binder.forField(mail)
                .asRequired()
                .bind(UserModel::getMail, UserModel::setMail);

        var cpf = new TextField("CPF");
        cpf.setAllowedCharPattern("[0-9]");
        cpf.setRequired(true);
        binder.forField(cpf)
                .asRequired()
                .bind(UserModel::getCpf, UserModel::setCpf);

        var phone = new TextField("Telefone");
        phone.setPattern("^[+]?[(]?[0-9]{3}[)]?[-s.]?[0-9]{3}[-s.]?[0-9]{4,6}$");
        phone.setAllowedCharPattern("[0-9]");
        phone.setMinLength(10);
        phone.setMaxLength(11);
        phone.setRequired(true);
        binder.forField(phone)
                .asRequired()
                .bind(UserModel::getPhone, UserModel::setPhone);

        var cancel = new Button("Cancelar");
        cancel.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addClickListener((event) -> this.close());

        var edit = new Button("Editar");
        edit.addClickListener(event -> edit());

        var layout = new FormLayout();
        layout.add(name, mail, cpf, phone);

        vlContent.removeAll();
        vlContent.add(layout);
        getFooter().removeAll();
        getFooter().add(cancel, edit);
        loadUserInformation();
    }

    private void loadUserInformation() {
        try {
            user = UserHelper.getLoggerUserModel();
            binder.readBean(UserHelper.getLoggerUserModel());
        } catch (Exception ex) {
            ex.printStackTrace();
            NotificationHelper.error(ex.getMessage());
        }
    }

    private void edit() {
        try {
            binder.writeBean(user);
            userService.save(user);
            NotificationHelper.success("Usuário editado com sucesso!");
            super.close();
            onEdit();
        } catch (ValidationException ex) {
            ex.printStackTrace();
            NotificationHelper.error("Alguns campos não foram preenchidos corretamente!");
        } catch (Exception e) {
            e.printStackTrace();
            NotificationHelper.error(e.getMessage());
        }
    }

    public void open() {
        initFields();
        super.open();
    }

    public void addUserEditedListener(UserEditedListener userEditedListener) {
        this.userEditedListenerList.add(userEditedListener);
    }

    public void onEdit() {
        for (var userEditedListner : this.userEditedListenerList) {
            userEditedListner.onEdit();
        }
    }
}
