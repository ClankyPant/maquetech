package com.example.application.components.material.consult;

import com.example.application.entities.user.UserEntity;
import com.example.application.listeners.material.EditMaterialListener;
import com.example.application.models.material.MaterialModel;
import com.example.application.services.material.MaterialService;
import com.example.application.services.user.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import javassist.NotFoundException;

import java.util.*;

public class MaterialConsultComponent extends VerticalLayout {

    private List<EditMaterialListener> editMaterialListenerList = new ArrayList<>();

    private final boolean isOnlyConsult;
    private final UserEntity user;
    private final MaterialService materialService;
    private final MaterialFilterComponent materialFilter;
    private final Grid<MaterialModel> grid = new Grid<>();
    private Map<Long, MaterialModel> mapMaterial = new HashMap<>();

    public MaterialConsultComponent(MaterialService materialService, UserService userService, boolean isOnlyConsult) throws NotFoundException {
        this.isOnlyConsult = isOnlyConsult;
        this.materialService = materialService;
        this.user = userService.getLoggedUser();
        this.materialFilter = new MaterialFilterComponent(materialService, user);

        init();
        initFilter();
        loadGridData();
    }

    private void init() {
        var btnConsult = new Button("Filtros");
        btnConsult.addClickListener(event -> materialFilter.open());

        grid.addColumn(MaterialModel::getName).setKey("MATERIAL_NAME").setHeader("Nome").setTextAlign(ColumnTextAlign.CENTER);
        grid.addColumn(MaterialModel::getStockQty).setKey("MATERIAL_STOCK").setHeader("Estoque").setTextAlign(ColumnTextAlign.CENTER);
        grid.addColumn(MaterialModel::getStockSafeQty).setKey("MATERIAL_STOCK_SAFE").setHeader("Estoque seguro").setTextAlign(ColumnTextAlign.CENTER).setVisible(isOnlyConsult);
        grid.addColumn(MaterialModel::getTypeDescription).setKey("MATERIAL_TYPE").setHeader("Tipo").setTextAlign(ColumnTextAlign.CENTER);
        grid.addColumn(MaterialModel::getUnitDescription).setKey("MATERIAL_UNIT").setHeader("Unidade").setTextAlign(ColumnTextAlign.CENTER);
        grid.addComponentColumn(MaterialModel::getReservationQuantityComponent).setKey("MATERIAL_QTY_RESERVATION").setHeader("Quantidade reserva").setTextAlign(ColumnTextAlign.CENTER).setVisible(!isOnlyConsult);
        grid.addComponentColumn(this::getGridActionColumn).setKey("MATERIAL_EDIT").setHeader("Ação").setTextAlign(ColumnTextAlign.CENTER);

        setSizeFull();
        add(btnConsult, grid);
    }

    private Component getGridActionColumn(MaterialModel materialModel) {
        if (this.isOnlyConsult) {
            return new Button("Editar", event -> editMaterial(materialModel.getId()));
        }

        return materialModel.getActionButtons();
    }

    private void initFilter() {
        materialFilter.addOpenedChangeListener(event -> {
            if (!event.isOpened()) {
                loadGridData();
            }
        });

        add(materialFilter);
    }

    private void loadGridData() {
        var materialType = materialFilter.getMaterialFilterModel().getType();

        var materialModelList = materialFilter.getMaterialFilterModel().getMaterialModelList();
        var materialCodeList = materialModelList.stream().map(MaterialModel::getId).toList();

        var listMaterialAux = new HashSet<MaterialModel>();
        var listMaterial = this.materialService.getList(materialCodeList, materialType, user.getType());
        if (listMaterial != null && !listMaterial.isEmpty()) {
            for (var material : listMaterial) {
                mapMaterial.computeIfAbsent(material.getId(), key -> material);

                listMaterialAux.add(
                    mapMaterial.computeIfPresent(material.getId(), (key, mat) -> {
                        mat.setStockQty(material.getStockQty());
                        return mat;
                    })
                );
            }
        }

        grid.setItems(listMaterialAux);
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
