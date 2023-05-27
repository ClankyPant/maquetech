package com.maquetech.application.components.user.search;

import com.maquetech.application.components.maquetech.grid.MaqueGrid;
import com.maquetech.application.components.user.UserEditComponent;
import com.maquetech.application.helpers.user.UserHelper;
import com.maquetech.application.models.user.UserModel;
import com.maquetech.application.services.user.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import javassist.NotFoundException;

public class UserSearchComponent extends VerticalLayout {

    private final UserEditComponent userEdit;
    private final UserFilterComponent filter;
    private final MaqueGrid<UserModel> grid = new MaqueGrid<>();

    public UserSearchComponent(UserService userService) throws NotFoundException {
        this.userEdit = new UserEditComponent(userService);
        this.filter = new UserFilterComponent(userService, UserHelper.getLoggerUserModel().getId());

        createGrid();
        init();
        search();
    }

    public void init() {
        setSizeFull();

        add(filter.getComponent());
        add(grid, userEdit);

        userEdit.addUserEditedListener(this::search);
        filter.addFilterSearchListener(() -> {
            grid.setItems(filter.getDataList());
        });
    }

    public void createGrid() {
        grid.setSizeFull();
        grid.addComponentColumn(this::getEdit).setKey("EDIT").setHeader("Editar").setTextAlign(ColumnTextAlign.CENTER);
        grid.addColumn(UserModel::getName).setKey("NAME").setHeader("Nome").setTextAlign(ColumnTextAlign.CENTER).setAutoWidth(true);
        grid.addColumn(UserModel::getCpf).setKey("CPF").setHeader("CPF").setTextAlign(ColumnTextAlign.CENTER).setAutoWidth(true);
        grid.addColumn(UserModel::getTypeDescription).setKey("TYPE").setHeader("Tipo").setTextAlign(ColumnTextAlign.CENTER).setAutoWidth(true);
        grid.addColumn(UserModel::getPhone).setKey("PHONE").setHeader("Telefone").setTextAlign(ColumnTextAlign.CENTER).setAutoWidth(true);
        grid.addColumn(UserModel::getMail).setKey("MAIL").setHeader("E-mail").setTextAlign(ColumnTextAlign.CENTER).setAutoWidth(true);
        grid.addColumn(UserModel::getCourseDescription).setKey("COURSE").setHeader("Curso").setTextAlign(ColumnTextAlign.CENTER).setAutoWidth(true);
    }

    public Component getEdit(UserModel user) {
        return new Button("Editar", VaadinIcon.PENCIL.create(), event -> {
            this.userEdit.open(user.getId());
        });
    }

    private void search() {
        this.filter.search();
    }
}
