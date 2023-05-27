package com.maquetech.application.components.user;

import com.maquetech.application.helpers.NotificationHelper;
import com.maquetech.application.helpers.user.UserHelper;
import com.maquetech.application.models.user.UserModel;
import com.maquetech.application.services.user.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import javassist.NotFoundException;

public class UserNavComponent extends HorizontalLayout {

    private Label navName;
    private final UserEditComponent userEdit;

    public UserNavComponent(UserService userService) throws NotFoundException {
        this.navName = new Label(UserHelper.getLoggerUserModel().getName());

        userEdit = new UserEditComponent(userService);
        userEdit.addUserEditedListener(() -> {
            try {
                this.navName.removeAll();
                this.navName.add(UserHelper.getLoggerUserModel().getName());
            } catch (Exception ex) {
                ex.printStackTrace();
                NotificationHelper.error(ex.getMessage());
            }
        });

        init();
    }

    private void init() {
        var menuBar = new MenuBar();
        var item = menuBar.addItem(this.navName);

        var subMenu = item.getSubMenu();

        var account = subMenu.addItem(getAccount());
        account.addClickListener(event -> userEdit.open());

        subMenu.add(new Hr());

        var singOut = subMenu.addItem(getSingOut());
        singOut.addClickListener(event -> UserHelper.logout());

        add(menuBar, userEdit);
        setAlignItems(Alignment.CENTER);
    }

    private Component getAccount() {
        var hlContent = new HorizontalLayout();
        hlContent.setJustifyContentMode(JustifyContentMode.BETWEEN);
        hlContent.add(VaadinIcon.USER.create());
        hlContent.add("Conta");
        return hlContent;
    }

    private Component getSingOut() {
        var hlContent = new HorizontalLayout();
        hlContent.setJustifyContentMode(JustifyContentMode.BETWEEN);
        hlContent.add(VaadinIcon.SIGN_OUT.create());
        hlContent.add("Sair");
        return hlContent;
    }
}
