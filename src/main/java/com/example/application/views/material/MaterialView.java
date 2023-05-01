package com.example.application.views.material;

import com.example.application.components.maquetech.MaqueVerticalLayout;
import com.example.application.components.material.MaterialCollectionTypeRegistrationComponent;
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

    public MaterialView(MaterialService materialService, CollectionTypeService collectionTypeService) {
        setAlignItems(Alignment.START);
        setJustifyContentMode(JustifyContentMode.START);

        TabSheet tabSheet = new TabSheet();
        tabSheet.setSizeFull();
//        tabSheet.add("Consulta", new MaterialConsultComponent());
        tabSheet.add("Cadastro", new MaterialRegistrationComponent(materialService, collectionTypeService));
        tabSheet.add("Cadastro (Tipo acervo)", new MaterialCollectionTypeRegistrationComponent(collectionTypeService));

        add(tabSheet);
    }
}
