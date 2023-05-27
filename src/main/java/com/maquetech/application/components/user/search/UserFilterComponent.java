package com.maquetech.application.components.user.search;

import com.maquetech.application.components.search.SearchComponent;
import com.maquetech.application.enums.user.UserTypeEnum;
import com.maquetech.application.helpers.NotificationHelper;
import com.maquetech.application.models.user.UserFilterModel;
import com.maquetech.application.models.user.UserModel;
import com.maquetech.application.services.user.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;

import java.util.List;

public class UserFilterComponent extends SearchComponent<UserFilterModel, UserModel> {

    private final Long loggedId;
    private final UserService userService;
    private final UserFilterModel filter = new UserFilterModel();

    public UserFilterComponent(UserService userService, Long loggedId) {
        this.loggedId = loggedId;
        this.userService = userService;
    }

    @Override
    public Component getComponent() {
        var type = new ComboBox<UserTypeEnum>();
        type.setClearButtonVisible(true);
        type.setItems(UserTypeEnum.values());
        type.setItemLabelGenerator(UserTypeEnum::getDescription);
        binder.forField(type).bind(UserFilterModel::getType, UserFilterModel::setType);

        var search = new Button("Consultar", VaadinIcon.SEARCH.create());
        search.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        search.addClickListener(event -> search());

        var layout = new FormLayout();
        layout.add(type, search);

        return layout;
    }

    public void search() {
        updateFilter(filter);

        this.dataList.clear();
        this.dataList.addAll(this.userService.getSearchList(filter.getType(), loggedId));

        afterSearch();
    }
}
