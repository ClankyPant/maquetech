package com.maquetech.application.views.material;

import com.maquetech.application.components.maquetech.MaqueVerticalLayout;
import com.maquetech.application.components.material.consult.MaterialSearchComponent;
import com.maquetech.application.services.material.MaterialService;
import com.maquetech.application.views.MainLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import javassist.NotFoundException;

@RolesAllowed("ADMIN")
@PageTitle("Materiais")
@Route(value = "material", layout = MainLayout.class)
public class MaterialView extends MaqueVerticalLayout {

    public MaterialView(MaterialService materialService) throws NotFoundException {
        setAlignItems(Alignment.START);
        setJustifyContentMode(JustifyContentMode.START);

        TabSheet tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        tabSheet.add("Consulta", new MaterialSearchComponent(materialService, true));

        add(tabSheet);
    }
}
