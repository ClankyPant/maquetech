package com.example.application.components.material;

import com.example.application.components.AbstractRegistrationComponent;
import com.example.application.entities.material.CollectionTypeEntity;
import com.example.application.helpers.NotificationHelper;
import com.example.application.services.material.CollectionTypeService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.TextField;
import org.apache.commons.lang3.StringUtils;

public class MaterialCollectionTypeRegistrationComponent extends AbstractRegistrationComponent<CollectionTypeEntity> {

    public MaterialCollectionTypeRegistrationComponent(CollectionTypeService collectionTypeService) {
        super();

        var collectionNameField = new TextField("Tipo de acervo");
        collectionNameField.setRequired(true);
        binder.forField(collectionNameField)
                .withValidator(StringUtils::isNotBlank, "Campo tipo de acervo precisa ser preenchido!")
                .bind(CollectionTypeEntity::getName, CollectionTypeEntity::setName);

        var registerButton = new Button("Cadastrar");
        registerButton.addClickListener(event -> {
            try {
                var collectionTypeEntity = new CollectionTypeEntity();
                binder.writeBean(collectionTypeEntity);

                collectionTypeService.create(collectionTypeEntity);

                binder.refreshFields();
                NotificationHelper.success("Tipo de acervo cadastrado com sucesso!");
            } catch (Exception ex) {
                NotificationHelper.error("Alguns campos não foram preenchidos corretamente!");
            }
        });

        formLayout.add(collectionNameField);
        vlContent.add(formLayout, registerButton);

        add(vlContent);
    }
}
