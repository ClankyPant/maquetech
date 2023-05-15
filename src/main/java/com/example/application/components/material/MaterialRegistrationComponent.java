package com.example.application.components.material;

import com.example.application.components.maquetech.MaqueVerticalLayout;
import com.example.application.entities.material.MaterialEntity;
import com.example.application.enums.material.MaterialTypeEnum;
import com.example.application.enums.material.MaterialUnitEnum;
import com.example.application.helpers.NotificationHelper;
import com.example.application.services.material.MaterialService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public class MaterialRegistrationComponent extends MaqueVerticalLayout {

    private static final Integer MATERIAL_NAME_MAX_LENGTH = 3;

    private MaterialEntity materialEntity;

    private final MaterialService materialService;

    private final Binder<MaterialEntity> binder = new Binder<>(MaterialEntity.class);

    public MaterialRegistrationComponent(MaterialService materialService) {
        this.materialService = materialService;

        var vlContent = new VerticalLayout();
        vlContent.setSizeFull();
        vlContent.setWidth("50%");

        var materialNameField = new TextField("Nome");
        materialNameField.setRequired(true);
        binder.forField(materialNameField)
                .withValidator(materialName -> materialName.length() > MATERIAL_NAME_MAX_LENGTH, "Nome do material deve conter mais do que " + MATERIAL_NAME_MAX_LENGTH + " caracteres!")
                .bind(MaterialEntity::getName, MaterialEntity::setName);

        var materialLocationField = new TextField("Localização do material");
        binder.forField(materialLocationField).bind(MaterialEntity::getLocation, MaterialEntity::setLocation);

        var materialStockQtyField = new NumberField("Quantidade em estoque");
        materialStockQtyField.setRequired(true);
        materialStockQtyField.setMin(0D);
        materialStockQtyField.setValue(0D);
        binder.forField(materialStockQtyField)
                .withValidator(Objects::nonNull, "Informe uma quantidade de estoque para o material!")
                .bind(MaterialEntity::getStockQty, MaterialEntity::setStockQty);

        var materialSafeStockQtyField = new NumberField("Quantidade em estoque (Segurança)");
        materialSafeStockQtyField.setMin(0D);
        binder.forField(materialSafeStockQtyField).bind(MaterialEntity::getStockSafeQty, MaterialEntity::setStockSafeQty);

        var materialUnitType = new ComboBox<MaterialUnitEnum>("Unidade material");
        materialUnitType.setItems(List.of(MaterialUnitEnum.UN, MaterialUnitEnum.BX, MaterialUnitEnum.ML, MaterialUnitEnum.MG));
        materialUnitType.setItemLabelGenerator(MaterialUnitEnum::getDescription);
        materialUnitType.setValue(MaterialUnitEnum.UN);
        materialUnitType.setRequired(true);
        binder.forField(materialUnitType).bind(MaterialEntity::getUnit, MaterialEntity::setUnit);

        var materialTypeField = new ComboBox<MaterialTypeEnum>("Tipo de material");
        materialTypeField.setItems(List.of(MaterialTypeEnum.TOOL, MaterialTypeEnum.DESK, MaterialTypeEnum.CONSUMABLE, MaterialTypeEnum.ENVIRONMENT));
        materialTypeField.setItemLabelGenerator(MaterialTypeEnum::getDescription);
        materialTypeField.setValue(MaterialTypeEnum.TOOL);
        materialTypeField.setRequired(true);
        binder.forField(materialTypeField).bind(MaterialEntity::getType, MaterialEntity::setType);

        var isOnlyProfessorField = new Checkbox("Apenas professores");
        binder.forField(isOnlyProfessorField).bind(MaterialEntity::getOnlyProfessor, MaterialEntity::setOnlyProfessor);

        var registerButton = new Button("Cadastrar / editar");
        registerButton.addClickListener(event -> {
            try {
                materialEntity = new MaterialEntity();
                binder.writeBean(materialEntity);
                materialService.create(materialEntity);

                materialEntity = null;
                binder.refreshFields();
                materialStockQtyField.setValue(0D);
                materialUnitType.setValue(MaterialUnitEnum.UN);
                materialTypeField.setValue(MaterialTypeEnum.TOOL);
                NotificationHelper.success("Material cadastrado com sucesso!");
            } catch (Exception ex) {
                ex.printStackTrace();
                NotificationHelper.error("Alguns campos não foram preenchidos corretamente!");
            }
        });

        var formLayout = new FormLayout();
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 3)
        );
        formLayout.add(materialNameField, materialLocationField, materialStockQtyField, materialSafeStockQtyField, materialUnitType, materialTypeField, isOnlyProfessorField);
        formLayout.setColspan(materialNameField, 3);
        formLayout.setColspan(materialTypeField, 3);
        formLayout.setColspan(materialLocationField, 3);
        vlContent.add(formLayout, registerButton);

        add(vlContent);
    }

    public void changeId(Long id) {
        materialEntity = this.materialService.getById(id);
        this.binder.readBean(materialEntity);
    }
}
