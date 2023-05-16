package com.example.application.components.material.consult;

import com.example.application.enums.material.MaterialTypeEnum;
import com.example.application.models.material.consult.MaterialFilterModel;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class MaterialFilterComponent extends VerticalLayout {

    private final Binder<MaterialFilterModel> binder = new Binder<>();

    public MaterialFilterComponent() {
        var codeField = new NumberField("Código");
        codeField.setStep(1);
        binder.forField(codeField)
                .withConverter(Double::longValue, Long::doubleValue)
                .bind(MaterialFilterModel::getCode, MaterialFilterModel::setCode);

        var likedNameField = new TextField("Nome");
        binder.forField(likedNameField)
                .bind(MaterialFilterModel::getName, MaterialFilterModel::setName);

        var materialTypeField = new ComboBox<MaterialTypeEnum>();
        materialTypeField.setClearButtonVisible(true);
        materialTypeField.setItems(MaterialTypeEnum.values());
        materialTypeField.setItemLabelGenerator(MaterialTypeEnum::getDescription);
        binder.forField(materialTypeField)
                .bind(MaterialFilterModel::getType, MaterialFilterModel::setType);

        add(new FormLayout(codeField, likedNameField, materialTypeField));
    }
}
