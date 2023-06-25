package com.maquetech.application.components.user.configuration;

import com.maquetech.application.helpers.NotificationHelper;
import com.maquetech.application.models.user.UserModel;
import com.maquetech.application.services.user.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class UserBulkChangeComponent extends Dialog {

    private Runnable afterSaveFunc;
    private final UserService userService;
    private final Set<UserModel> setUserModel = new HashSet<>();
    private final InMemoryUserDetailsManager inMemoryUserDetailsManager;

    private ComboBox<Boolean> situation;
    private final Button save = new Button("Salvar");
    private final Button cancel = new Button("Cancelar");
    private final Button bulkChange = new Button("Alteração em massa");

    public  UserBulkChangeComponent(UserService userService, InMemoryUserDetailsManager inMemoryUserDetailsManager) {
        this.userService = userService;
        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;

        init();
        initFooter();
    }

    private void init() {
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        bulkChange.setEnabled(false);
        bulkChange.addClickListener(event -> {

        });

        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);

        situation = new ComboBox<>("Situação");
        situation.setItems(Boolean.TRUE, Boolean.FALSE);
        situation.setValue(Boolean.TRUE);
        situation.setRequired(true);
        situation.setRequiredIndicatorVisible(true);
        situation.setItemLabelGenerator(this::getLabelDescription);
        situation.addValueChangeListener(event -> {
            situation.setValue(Objects.isNull(situation.getValue()) ? Boolean.TRUE : situation.getValue());
        });

        add(situation);
    }

    private void initFooter() {
        save.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        save.addClickListener(event -> save());

        cancel.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addClickListener(event -> this.close());

        getFooter().add(cancel, save);
    }

    public void changeEnable(boolean enable) {
        this.bulkChange.setEnabled(enable);
    }

    public Component getBulkChangeButton() {
        return bulkChange;
    }

    public void addClickEvent(Runnable func) {
        this.bulkChange.addClickListener(event -> func.run());
    }

    public void openBulkChange(Set<UserModel> setUserModel) {
        try {
            if (CollectionUtils.isEmpty(setUserModel)) throw new IllegalArgumentException("Selecione ao menos um usuário para alteração em massa!");
            this.setUserModel.clear();
            this.setUserModel.addAll(setUserModel);

            super.open();

        } catch (Exception ex) {
            ex.printStackTrace();
            NotificationHelper.error(ex.getMessage());
        }
    }

    @Override
    public void open() {
        throw new NotImplementedException();
    }

    private String getLabelDescription(Boolean situation) {
        return situation ? "Ativo" : "Inativo";
    }

    private void save() {
        try {
            this.userService.bulkChange(this.setUserModel, this.situation.getValue(), inMemoryUserDetailsManager);
            NotificationHelper.success("Usuários alterados com sucesso!");
        } catch (Exception ex) {
            ex.printStackTrace();
            NotificationHelper.error(ex.getMessage());
        } finally {
            try {
                this.afterSaveFunc.run();
            } catch (Exception ex) {
                ex.printStackTrace();
                NotificationHelper.error(ex.getMessage());
            }

            this.close();
        }
    }

    public void addAfterSaveFunc(Runnable func) {
        this.afterSaveFunc = func;
    }
}
