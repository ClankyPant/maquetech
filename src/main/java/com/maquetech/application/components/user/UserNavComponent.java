package com.maquetech.application.components.user;

import com.maquetech.application.Application;
import com.maquetech.application.entities.user.UserEntity;
import com.maquetech.application.helpers.user.UserHelper;
import com.maquetech.application.services.user.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import javassist.NotFoundException;

public class UserNavComponent extends HorizontalLayout {

    private UserEntity loggedUser = UserHelper.getLoggedUser();
    private final UserEditComponent userConfigurationComponent;

    public UserNavComponent(UserService userService) throws NotFoundException {
        userConfigurationComponent = new UserEditComponent(userService);

        init();
    }

    private void init() {
        var menuBar = new MenuBar();
        var item = menuBar.addItem(loggedUser.getName());

        var subMenu = item.getSubMenu();

        var account = subMenu.addItem(getAccount());
        account.addClickListener(event -> userConfigurationComponent.open());

        subMenu.add(new Hr());

        var singOut = subMenu.addItem(getSingOut());
        singOut.addClickListener(event -> UserHelper.logout());

        add(menuBar, userConfigurationComponent);
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
