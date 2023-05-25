package com.maquetech.application.components.user;

import com.maquetech.application.Application;
import com.maquetech.application.entities.user.UserEntity;
import com.maquetech.application.helpers.UserHelper;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import javassist.NotFoundException;

import java.util.Random;

public class UserConfigurationComponent extends HorizontalLayout {

    private UserEntity loggedUser = UserHelper.getLoggedUser();

    public UserConfigurationComponent() throws NotFoundException {
        init();
    }

    private void init() {
        var menuBar = new MenuBar();
        var item = menuBar.addItem(loggedUser.getName());

        var subMenu = item.getSubMenu();
        subMenu.addItem(getAccount());
        subMenu.add(new Hr());
        subMenu.addItem(getSingOut());

        add(menuBar);
        setAlignItems(Alignment.CENTER);
    }

    private Component getAccount() {
        var label = new Span("Conta");
        var icon = VaadinIcon.USER.create();
        icon.setSize("15px");

        var hlContent = new HorizontalLayout();
        hlContent.setJustifyContentMode(JustifyContentMode.BETWEEN);
        hlContent.add(icon, label);
        return hlContent;
    }

    private Component getSingOut() {
        var label = new Span("Sair");
        var icon = VaadinIcon.SIGN_OUT.create();
        icon.setSize("15px");

        var hlContent = new HorizontalLayout();
        hlContent.setJustifyContentMode(JustifyContentMode.BETWEEN);
        hlContent.addClickListener(event -> {
            Application.AUTHENTICATION_CONTEXT.logout();
        });
        hlContent.add(icon, label);
        return hlContent;
    }
}