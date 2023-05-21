package com.maquetech.application.components.material.consult;

import com.maquetech.application.entities.user.UserEntity;
import com.maquetech.application.helpers.ConvertHelper;
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

public class MaterialConsultComponent extends VerticalLayout {

    private Date endBookingDate;
    private Date startBookingDate;

    private final UserEntity user;
    private final boolean isOnlyConsult;
    private final MaterialService materialService;
    private final MaterialFilterComponent materialFilter;
    private final Grid<MaterialModel> grid = new Grid<>();
    private final Map<Long, MaterialModel> materialMap = new HashMap<>();
    private final List<EditMaterialListener> editMaterialListenerList = new ArrayList<>();

    public MaterialConsultComponent(
            MaterialService materialService,
            UserService userService,
            boolean isOnlyConsult
    ) throws NotFoundException {
        this.isOnlyConsult = isOnlyConsult;
        this.materialService = materialService;
        this.user = userService.getLoggedUser();
        this.materialFilter = new MaterialFilterComponent(materialService, user);

        init();
        initFilter();
        loadGrid();
    }

    private void init() {
        grid.addComponentColumn(MaterialModel::getReservationQuantityComponent).setKey("MATERIAL_QTY_RESERVATION").setHeader("Qtde").setTextAlign(ColumnTextAlign.CENTER).setVisible(!isOnlyConsult);
        grid.addComponentColumn(this::getActionColumn).setKey("MATERIAL_EDIT").setHeader("Ação").setTextAlign(ColumnTextAlign.CENTER);
        grid.addColumn(MaterialModel::getName).setKey("MATERIAL_NAME").setHeader("Nome").setTextAlign(ColumnTextAlign.CENTER).setAutoWidth(true);
        grid.addColumn(MaterialModel::getStockQty).setKey("MATERIAL_STOCK").setHeader("Estoque").setTextAlign(ColumnTextAlign.CENTER).setAutoWidth(true);
        grid.addColumn(MaterialModel::getStockSafeQty).setKey("MATERIAL_STOCK_SAFE").setHeader("Estoque seguro").setTextAlign(ColumnTextAlign.CENTER).setVisible(isOnlyConsult);
        grid.addColumn(MaterialModel::getTypeDescription).setKey("MATERIAL_TYPE").setHeader("Tipo").setTextAlign(ColumnTextAlign.CENTER).setAutoWidth(true);
        grid.addColumn(MaterialModel::getUnitDescription).setKey("MATERIAL_UNIT").setHeader("Unidade").setTextAlign(ColumnTextAlign.CENTER).setAutoWidth(true);

        setSizeFull();
        add(new Button("Filtros", event -> materialFilter.open()), grid);
    }

    private Component getActionColumn(MaterialModel materialModel) {
        if (this.isOnlyConsult) {
            return new Button("Editar", event -> editMaterial(materialModel.getId()));
        }

        return materialModel.getReservationActionButtons();
    }

    private void initFilter() {
        materialFilter.addOpenedChangeListener(event -> {
            if (!event.isOpened()) {
                loadGrid();
            }
        });

        add(materialFilter);
    }

    private void loadGrid() {
        grid.setItems(updateMaterialMap());
    }

    private HashSet<MaterialModel> updateMaterialMap() {
        var materialType = materialFilter.getMaterialFilterModel().getType();

        var materialModelList = materialFilter.getMaterialFilterModel().getMaterialModelList();
        var materialIdList = materialModelList.stream().map(MaterialModel::getId).toList();

        var materialListAux = new HashSet<MaterialModel>();
        var materialList = this.materialService.getList(
                materialIdList,
                materialType,
                user.getType(),
                isOnlyConsult,
                this.startBookingDate,
                this.endBookingDate
        );

        if (CollectionUtils.isNotEmpty(materialList)) {
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

        return materialListAux;
    }

    public void addEditMaterialListener(EditMaterialListener listener) {
        this.editMaterialListenerList.add(listener);
    }

    public void editMaterial(Long id) {
        for (var listener : this.editMaterialListenerList) {
            listener.edit(id);
        }
    }

    public void resetReservationConsult(Date startBookingDate, Date endBookingDate) {
        this.startBookingDate = startBookingDate;
        this.endBookingDate = endBookingDate;

        this.materialFilter.resetConfiguration();
        this.materialMap.clear();
        loadGrid();
    }

    private List<MaterialModel> getOnReservationList() {
        return this.materialMap.values().stream().filter(MaterialModel::isOnReservation).toList();
    }

    public List<MaterialModel> getOnReservationListAndValidate() {
        this.materialFilter.resetConfiguration();
        loadGrid();

        var materialModelList = this.getOnReservationList();

        if (CollectionUtils.isEmpty(materialModelList)) {
            throw new IllegalArgumentException("Adicione ao menos um material à reserva!");
        }

        var materialModelIdList = materialModelList.stream().map(MaterialModel::getId).toList();
        var materialEntityMap = materialService.getMapById(materialModelIdList);

        for (var materialModel : materialModelList) {
            var materialId = materialModel.getId();
            var materialEntity = materialEntityMap.getOrDefault(materialId, null);

            if (materialEntity == null) throw new IllegalArgumentException("Material " + materialId + " não encontrado");

            var reservationQuantity = ConvertHelper.getDouble(materialModel.getReservationQuantity(), 0D);
            var stockQuantity = ConvertHelper.getDouble(materialModel.getStockQty(), 0D);

            if (stockQuantity < reservationQuantity) throw new IllegalArgumentException(materialModel.getName() + " não possui estoque suficiente. Estoque atual: " + stockQuantity + "!");

            materialModel.setEntidade(materialEntity);
        }

        return materialModelList;
    }
}
