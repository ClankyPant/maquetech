package com.maquetech.application.components.material.consult;

import com.maquetech.application.entities.user.UserEntity;
import com.maquetech.application.enums.material.MaterialTypeEnum;
import com.maquetech.application.helpers.NotificationHelper;
import com.maquetech.application.models.material.MaterialModel;
import com.maquetech.application.models.material.consult.MaterialFilterModel;
import com.maquetech.application.services.material.MaterialService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.theme.material.Material;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;

import java.util.*;


@Getter
public class MaterialFilterComponent {

    private final UserEntity user;
    private final MaterialService materialService;
    private final Binder<MaterialFilterModel> binder = new Binder<>();

    public MaterialFilterModel materialFilterModel = new MaterialFilterModel();
    public Button btnSearch = new Button("Consultar", VaadinIcon.SEARCH.create());

    public MaterialFilterComponent(MaterialService materialService, UserEntity user) {
        this.materialService = materialService;
        this.user = user;

        getComponentBody();
    }

    public Component[] getComponentBody() {
        var materialFilter = new MultiSelectComboBox<MaterialModel>("Material");
        materialFilter.setClearButtonVisible(true);
        materialFilter.setItemsWithFilterConverter(
                query -> materialService.getListByPage(query.getFilter().orElse(""), user.getType(),  PageRequest.of(query.getPage(), query.getLimit())).stream(),
                term -> term
        );
        materialFilter.setItemLabelGenerator(MaterialModel::getName);
        binder.forField(materialFilter)
                .withConverter(this::toList, this::toSet)
                .bind(MaterialFilterModel::getMaterialModelList, MaterialFilterModel::setMaterialModelList);

        var materialTypeField = new ComboBox<MaterialTypeEnum>("Tipo de material");
        materialTypeField.setClearButtonVisible(true);
        materialTypeField.setItems(MaterialTypeEnum.values());
        materialTypeField.setItemLabelGenerator(MaterialTypeEnum::getDescription);
        binder.forField(materialTypeField)
                .bind(MaterialFilterModel::getType, MaterialFilterModel::setType);

        btnSearch.addClickListener(buttonClickEvent -> {
            try {
                binder.writeBean(materialFilterModel);
            } catch (ValidationException ex) {
                ex.printStackTrace();
                NotificationHelper.error(ex.getMessage());
            }
        });
        btnSearch.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

        return new Component[] { materialFilter, materialTypeField, btnSearch };
    }

    private List<MaterialModel> toList(Set<MaterialModel> data) {
        return new ArrayList<>(data);
    }

    private Set<MaterialModel> toSet(List<MaterialModel> data) {
        return new HashSet<>(data);
    }

    public void resetConfiguration() {
        try {
            this.binder.refreshFields();
            this.binder.writeBean(this.materialFilterModel);
        } catch (Exception ex) {
            ex.printStackTrace();
            NotificationHelper.error(ex.getMessage());
        }
    }

    public MaterialTypeEnum getMaterialType() {
        return this.materialFilterModel.getType();
    }

    public List<MaterialModel> getMaterialModelList() {
        return this.materialFilterModel.getMaterialModelList();
    }

    public List<Long> getMaterialModelIdList() {
        return getMaterialModelList().stream().map(MaterialModel::getId).toList();
    }
}
