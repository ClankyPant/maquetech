package com.maquetech.application.components.material.consult;

import com.maquetech.application.entities.user.UserEntity;
import com.maquetech.application.helper.ConvertHelper;
import com.maquetech.application.listeners.material.EditMaterialListener;
import com.maquetech.application.models.material.MaterialModel;
import com.maquetech.application.services.material.MaterialService;
import com.maquetech.application.services.user.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import javassist.NotFoundException;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

public class MaterialConsultComponent extends VerticalLayout {

    private List<EditMaterialListener> editMaterialListenerList = new ArrayList<>();

    private final boolean isOnlyConsult;
    private final UserEntity user;
    private final MaterialService materialService;
    private final MaterialFilterComponent materialFilter;
    private final Grid<MaterialModel> grid = new Grid<>();
    private Map<Long, MaterialModel> materialMap = new HashMap<>();

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
        loadGridData(null);
    }

    private void loadGridData(List<Long> materialIdList) {
        var materialType = materialFilter.getMaterialFilterModel().getType();

        var materialModelList = materialFilter.getMaterialFilterModel().getMaterialModelList();
        if (CollectionUtils.isEmpty(materialIdList)) {
            materialIdList = materialModelList.stream().map(MaterialModel::getId).toList();
        }

        var materialListAux = new HashSet<MaterialModel>();
        var materialList = this.materialService.getList(materialIdList, materialType, user.getType(), isOnlyConsult);;
        if (materialList != null && !materialList.isEmpty()) {
            for (var material : materialList) {
                materialMap.computeIfAbsent(material.getId(), key -> material);

                materialListAux.add(
                    materialMap.computeIfPresent(material.getId(), (key, mat) -> {
                        mat.setStockQty(material.getStockQty());
                        return mat;
                    })
                );
            }
        }

        grid.setItems(materialListAux);
    }

    public void addEditMaterialListener(EditMaterialListener listener) {
        this.editMaterialListenerList.add(listener);
    }

    public void editMaterial(Long id) {
        for (var listener : this.editMaterialListenerList) {
            listener.edit(id);
        }
    }

    public void resetConfiguration() {
        this.materialFilter.resetConfiguration();
        this.materialMap.clear();
        loadGridData();
    }

    private List<MaterialModel> getOnReservationList() {
        return this.materialMap.values().stream().filter(MaterialModel::isOnReservation).toList();
    }

    public List<MaterialModel> getOnReservationListAndValidate() {
        var materialModelList = this.getOnReservationList();

        if (CollectionUtils.isEmpty(materialModelList)) {
            throw new IllegalArgumentException("Adicione ao menos um material à reserva!");
        }

        var materialModelIdList = materialModelList.stream().map(MaterialModel::getId).collect(Collectors.toList());
        var materialMap = materialService.getMapById(materialModelIdList);

        for (var materialModel : materialModelList) {
            var materialId = materialModel.getId();
            var material = materialMap.getOrDefault(materialId, null);

            if (material == null) {
                throw new IllegalArgumentException("Material " + materialId + " não encontrado");
            }

            var reservationQuantity = ConvertHelper.getDouble(materialModel.getReservationQuantity(), 0D);
            var stockQuantity = ConvertHelper.getDouble(material.getStockQty(), 0D);

            if (stockQuantity < reservationQuantity) {
                throw new IllegalArgumentException(materialModel.getName() + " não possui estoque suficiente. Estoque atual: " + stockQuantity + "!");
            }

            materialModel.setEntidade(material);
        }

        return materialModelList;
    }
}
