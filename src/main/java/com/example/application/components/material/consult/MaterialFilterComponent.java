package com.example.application.components.material.consult;

import com.example.application.enums.material.MaterialTypeEnum;
import com.example.application.helpers.NotificationHelper;
import com.example.application.models.material.MaterialModel;
import com.example.application.models.material.consult.MaterialFilterModel;
import com.example.application.services.material.MaterialService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
public class MaterialFilterComponent extends Dialog {

    private final MaterialService materialService;
    private final Binder<MaterialFilterModel> binder = new Binder<>();
    public MaterialFilterModel materialFilterModel = new MaterialFilterModel();

    public MaterialFilterComponent(MaterialService materialService) {
        this.materialService = materialService;
        init();
    }

    private void init() {
        var materialFilter = new MultiSelectComboBox<MaterialModel>("Material");
        materialFilter.setClearButtonVisible(true);
        materialFilter.setItemsWithFilterConverter(
                query -> materialService.getListByPage(query.getFilter().orElse(""), PageRequest.of(query.getPage(), query.getLimit())).stream(),
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


        setWidth("65%");
        add(new FormLayout(materialFilter, materialTypeField));
        getFooter().add(new Button("Filtrar", event -> {
            try {
                binder.writeBean(materialFilterModel);
                super.close();
            } catch (ValidationException e) {
                NotificationHelper.error(e.getMessage());
            }
        }));
    }

    private List<MaterialModel> toList(Set<MaterialModel> data) {
        return new ArrayList<>(data);
    }

    private Set<MaterialModel> toSet(List<MaterialModel> data) {
        return new HashSet<>(data);
    }
}