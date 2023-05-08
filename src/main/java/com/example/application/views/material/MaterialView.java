package com.example.application.views.material;

import com.example.application.components.maquetech.MaqueVerticalLayout;
import com.example.application.components.material.MaterialCollectionTypeRegistrationComponent;
import com.example.application.components.material.MaterialConsultComponent;
import com.example.application.components.material.MaterialRegistrationComponent;
import com.example.application.services.material.CollectionTypeService;
import com.example.application.services.material.MaterialService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed("ADMIN")
@PageTitle("Materiais")
@Route(value = "material", layout = MainLayout.class)
public class MaterialView extends MaqueVerticalLayout {

    private final TabSheet tabSheet = new TabSheet();

    private final MaterialConsultComponent materialConsultComponent;

    private final MaterialRegistrationComponent materialRegistrationComponent;


    public MaterialView(MaterialService materialService, CollectionTypeService collectionTypeService) {
        setAlignItems(Alignment.START);
        setJustifyContentMode(JustifyContentMode.START);

        materialRegistrationComponent = new MaterialRegistrationComponent(materialService, collectionTypeService);

        materialConsultComponent = new MaterialConsultComponent(materialService);
        materialConsultComponent.addEditMaterialListener(id -> {
            tabSheet.setSelectedIndex(1);
            materialRegistrationComponent.changeId(id);
        });

        tabSheet.setSizeFull();
        tabSheet.add("Consulta", materialConsultComponent);
        tabSheet.add("Cadastro", materialRegistrationComponent);
        tabSheet.add("Cadastro (Tipo acervo)", new MaterialCollectionTypeRegistrationComponent(collectionTypeService));

        add(tabSheet);
    }
}
