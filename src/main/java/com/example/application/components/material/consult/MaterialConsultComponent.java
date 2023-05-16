package com.example.application.components.material.consult;

import com.example.application.entities.material.MaterialEntity;
import com.example.application.entities.user.UserEntity;
import com.example.application.helpers.NotificationHelper;
import com.example.application.listeners.material.EditMaterialListener;
import com.example.application.services.material.MaterialService;
import com.example.application.services.user.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import javassist.NotFoundException;

import java.util.ArrayList;
import java.util.List;

public class MaterialConsultComponent extends VerticalLayout {

    private List<EditMaterialListener> editMaterialListenerList = new ArrayList<>();

    private final UserEntity loggedUser;
    private final UserService userService;
    private final MaterialService materialService;
    private final Grid<MaterialEntity> grid = new Grid<MaterialEntity>();

    public MaterialConsultComponent(MaterialService materialService, UserService userService) throws NotFoundException {
        this.userService = userService;
        this.materialService = materialService;
        this.loggedUser = userService.getLoggedUser();

        var formLayout = new FormLayout();
        var btnConsult = new Button("Consultar");
        btnConsult.addClickListener(event -> {
            grid.setItems(this.materialService.getAllByUserType(loggedUser.getType()));
            NotificationHelper.success("Consulta feito com sucesso!");
        });
        formLayout.add(btnConsult);

        grid.setItems(this.materialService.getAllByUserType(loggedUser.getType()));
        grid.addColumn(MaterialEntity::getName).setKey("MATERIAL_NAME").setHeader("Nome");
        grid.addColumn(MaterialEntity::getStockQty).setKey("MATERIAL_STOCK").setHeader("Qtd. estoque");
        grid.addColumn(MaterialEntity::getStockSafeQty).setKey("MATERIAL_STOCK_SAFE").setHeader("Qtd. estoque seg.");
        grid.addColumn(materialEntity -> materialEntity.getType().getDescription()).setKey("MATERIAL_TYPE").setHeader("Tipo");
        grid.addColumn(materialEntity -> materialEntity.getUnit().getDescription()).setKey("MATERIAL_UNIT").setHeader("Unidade");
        grid.addComponentColumn(materialEntity -> {
            var btnEdit = new Button("Editar");
            btnEdit.addClickListener(event -> editMaterial(materialEntity.getId()));
            return btnEdit;
        }).setKey("MATERIAL_EDIT").setHeader("Ação");


        add(formLayout, grid);
    }

    public void addEditMaterialListener(EditMaterialListener listener) {
        this.editMaterialListenerList.add(listener);
    }

    public void editMaterial(Long id) {
        for (var listener : this.editMaterialListenerList) {
            listener.edit(id);
        }
    }
}
