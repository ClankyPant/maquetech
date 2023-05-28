package com.maquetech.application.components.material.search;

import com.maquetech.application.components.maquetech.grid.MaqueGrid;
import com.maquetech.application.components.material.MaterialRegistrationComponent;
import com.maquetech.application.entities.user.UserEntity;
import com.maquetech.application.helpers.ConvertHelper;
import com.maquetech.application.helpers.user.UserHelper;
import com.maquetech.application.models.material.MaterialModel;
import com.maquetech.application.services.material.MaterialService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import javassist.NotFoundException;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

public class MaterialSearchComponent extends VerticalLayout {

    private Date endBookingDate;
    private Date startBookingDate;

    private final UserEntity loggedUser;
    private final boolean isOnlyConsult;
    private final MaterialService materialService;
    private final MaterialFilterComponent materialFilter;
    private final MaqueGrid<MaterialModel> grid = new MaqueGrid<>();
    private final Map<Long, MaterialModel> materialMap = new HashMap<>();
    private final MaterialRegistrationComponent materialRegistrationComponent;

    public MaterialSearchComponent(
            MaterialService materialService,
            boolean isOnlyConsult
    ) throws NotFoundException {
        this.isOnlyConsult = isOnlyConsult;
        this.materialService = materialService;
        this.loggedUser = UserHelper.getLoggedUser();
        this.materialFilter = new MaterialFilterComponent(materialService, loggedUser);
        this.materialRegistrationComponent = new MaterialRegistrationComponent(materialService);
        this.materialRegistrationComponent.addDialogCloseListenerList(this::loadGrid);

        init();
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
        add(getHeader(), grid, materialRegistrationComponent);
    }

    private Component getHeader() {
        materialFilter.getBtnSearch().addClickListener(buttonClickEvent -> loadGrid());

        var btnNew = new Button("Novo", VaadinIcon.PLUS.create(), event -> materialRegistrationComponent.open(null));
        btnNew.setVisible(loggedUser.isAdmin());

        var hlContent = new FormLayout();
        hlContent.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0px", 1),
                new FormLayout.ResponsiveStep("1000px", 4)
        );
        hlContent.setWidth("100%");
        hlContent.add(materialFilter.getComponentBody());
        hlContent.add(btnNew);
        return hlContent;
    }

    private Component getActionColumn(MaterialModel materialModel) {
        if (this.isOnlyConsult) {
            return new Button("Editar", event -> materialRegistrationComponent.open(materialModel.getId()));
        }

        return materialModel.getReservationActionButtons();
    }

    private void loadGrid() {
        grid.setItems(updateMaterialMap());
    }

    private HashSet<MaterialModel> updateMaterialMap() {
        var materialListAux = new HashSet<MaterialModel>();
        var materialList = this.materialService.getList(
                materialFilter.getMaterialModelIdList(),
                materialFilter.getMaterialType(),
                loggedUser.getType(),
                isOnlyConsult,
                this.startBookingDate,
                this.endBookingDate
        );

        if (CollectionUtils.isNotEmpty(materialList)) {
            for (var material : materialList) {
                materialMap.computeIfAbsent(material.getId(), key -> material);

                materialListAux.add(
                        materialMap.computeIfPresent(material.getId(), (key, mat) -> {
                            mat.setStockSafeQty(material.getStockSafeQty());
                            mat.setStockQty(material.getStockQty());
                            mat.setName(material.getName());
                            mat.setUnit(material.getUnit());
                            mat.setType(material.getType());
                            return mat;
                        })
                );
            }
        }

        return materialListAux;
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
