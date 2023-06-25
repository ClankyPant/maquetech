package com.maquetech.application.components.user.search;

import com.maquetech.application.components.maquetech.grid.MaqueGrid;
import com.maquetech.application.components.user.UserEditComponent;
import com.maquetech.application.components.user.UserRegistrationComponent;
import com.maquetech.application.components.user.configuration.UserBulkChangeComponent;
import com.maquetech.application.helpers.user.UserHelper;
import com.maquetech.application.models.user.UserModel;
import com.maquetech.application.services.course.CourseService;
import com.maquetech.application.services.user.UserService;
import com.maquetech.application.services.user.professor.ProfessorCodeService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import javassist.NotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

public class UserSearchComponent extends VerticalLayout {

    private final UserEditComponent userEdit;
    private final UserFilterComponent filter;
    private final UserRegistrationComponent userRegistration;
    private final MaqueGrid<UserModel> grid = new MaqueGrid<>();
    private final UserBulkChangeComponent bulkChange;

    public UserSearchComponent(UserService userService, CourseService courseService, ProfessorCodeService professorCodeService, InMemoryUserDetailsManager inMemoryUserDetailsManager) throws NotFoundException {
        this.bulkChange = new UserBulkChangeComponent(userService, inMemoryUserDetailsManager);
        this.userRegistration = new UserRegistrationComponent(userService, courseService, professorCodeService, inMemoryUserDetailsManager);
        this.userEdit = new UserEditComponent(userService, inMemoryUserDetailsManager);
        this.filter = new UserFilterComponent(userService, UserHelper.getLoggerUserModel().getId());

        createGrid();
        init();
        search();
    }

    public void init() {
        setSizeFull();

        bulkChange.addClickEvent(() -> bulkChange.openBulkChange(grid.getSelectedItems()));
        bulkChange.addAfterSaveFunc(this::filter);

        add(filter.getComponent(bulkChange.getBulkChangeButton(), new Button("Novo", VaadinIcon.PLUS.create(), event -> userRegistration.open())));
        add(grid, userEdit);

        userEdit.addDialogCloseListener(this::search);
        filter.addFilterSearchListener(this::filter);
    }

    private void filter() {
        grid.setItems(filter.getDataList());
    }

    public void createGrid() {
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addComponentColumn(this::getEdit).setKey("EDIT").setHeader("Editar").setTextAlign(ColumnTextAlign.CENTER);
        grid.addColumn(UserModel::getName).setKey("NAME").setHeader("Nome").setTextAlign(ColumnTextAlign.CENTER).setAutoWidth(true);
        grid.addColumn(UserModel::getCpf).setKey("CPF").setHeader("CPF").setTextAlign(ColumnTextAlign.CENTER).setAutoWidth(true);
        grid.addColumn(UserModel::getSituationDisplay).setKey("SITUATION").setHeader("Situação").setTextAlign(ColumnTextAlign.CENTER).setAutoWidth(true);
        grid.addColumn(UserModel::getTypeDescription).setKey("TYPE").setHeader("Tipo").setTextAlign(ColumnTextAlign.CENTER).setAutoWidth(true);
        grid.addColumn(UserModel::getPhone).setKey("PHONE").setHeader("Telefone").setTextAlign(ColumnTextAlign.CENTER).setAutoWidth(true);
        grid.addColumn(UserModel::getMail).setKey("MAIL").setHeader("E-mail").setTextAlign(ColumnTextAlign.CENTER).setAutoWidth(true);
        grid.addColumn(UserModel::getCourseDescription).setKey("COURSE").setHeader("Curso").setTextAlign(ColumnTextAlign.CENTER).setAutoWidth(true);
        grid.addSelectionListener(event -> {
           var listSelected = event.getAllSelectedItems();
           bulkChange.changeEnable(listSelected.size() > 0);
        });
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
