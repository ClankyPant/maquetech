package com.example.application.components.material.consult;

import com.example.application.entities.user.UserEntity;
import com.example.application.listeners.material.EditMaterialListener;
import com.example.application.models.material.MaterialModel;
import com.example.application.services.material.MaterialService;
import com.example.application.services.user.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import javassist.NotFoundException;

import java.util.ArrayList;
import java.util.List;

public class MaterialConsultComponent extends VerticalLayout {

    private List<EditMaterialListener> editMaterialListenerList = new ArrayList<>();

    private final UserEntity loggedUser;
    private final MaterialService materialService;
    private final MaterialFilterComponent materialFilter;
    private final Grid<MaterialModel> grid = new Grid<>();

    public MaterialConsultComponent(MaterialService materialService, UserService userService) throws NotFoundException {
        this.materialService = materialService;
        this.loggedUser = userService.getLoggedUser();
        this.materialFilter = new MaterialFilterComponent(materialService);

        init();
        initFilter();
    }

    private void init() {
        var btnConsult = new Button("Filtros");
        btnConsult.addClickListener(event -> {
            materialFilter.open();
        });

        grid.setItems(this.materialService.getList(loggedUser.getType()));
        grid.addColumn(MaterialModel::getName).setKey("MATERIAL_NAME").setHeader("Nome");
        grid.addColumn(MaterialModel::getStockQty).setKey("MATERIAL_STOCK").setHeader("Qtd. estoque");
        grid.addColumn(MaterialModel::getStockSafeQty).setKey("MATERIAL_STOCK_SAFE").setHeader("Qtd. estoque seg.");
        grid.addColumn(MaterialModel::getTypeDescription).setKey("MATERIAL_TYPE").setHeader("Tipo");
        grid.addColumn(MaterialModel::getUnitDescription).setKey("MATERIAL_UNIT").setHeader("Unidade");
        grid.addComponentColumn(materialEntity -> {
            var btnEdit = new Button("Editar");
            btnEdit.addClickListener(event -> editMaterial(materialEntity.getId()));
            return btnEdit;
        }).setKey("MATERIAL_EDIT").setHeader("Ação");

        add(btnConsult, grid);
    }

    private void initFilter() {
        materialFilter.addOpenedChangeListener(event -> {
            if (!event.isOpened()) {
                var materialType = materialFilter.getMaterialFilterModel().getType();

                var materialModelList = materialFilter.getMaterialFilterModel().getMaterialModelList();
                var materialCodeList = materialModelList.stream().map(MaterialModel::getId).toList();

                grid.setItems(this.materialService.getList(materialCodeList, materialType, loggedUser.getType()));
            }
        });

        add(materialFilter);
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
