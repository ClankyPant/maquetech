package com.example.application.views.material;

import com.example.application.components.maquetech.MaqueVerticalLayout;
import com.example.application.components.material.consult.MaterialConsultComponent;
import com.example.application.components.material.MaterialRegistrationComponent;
import com.example.application.services.material.MaterialService;
import com.example.application.services.user.UserService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import javassist.NotFoundException;

@RolesAllowed("ADMIN")
@PageTitle("Materiais")
@Route(value = "material", layout = MainLayout.class)
public class MaterialView extends MaqueVerticalLayout {

    private final TabSheet tabSheet = new TabSheet();

    private final MaterialRegistrationComponent materialRegistrationComponent;


    public MaterialView(MaterialService materialService, UserService userService) throws NotFoundException {
        setAlignItems(Alignment.START);
        setJustifyContentMode(JustifyContentMode.START);

        materialRegistrationComponent = new MaterialRegistrationComponent(materialService);

        var materialConsultComponent = new MaterialConsultComponent(materialService, userService);
        materialConsultComponent.addEditMaterialListener(id -> {
            tabSheet.setSelectedIndex(1);
            materialRegistrationComponent.changeId(id);
        });

        tabSheet.setSizeFull();
        tabSheet.add("Consulta", materialConsultComponent);
        tabSheet.add("Cadastro", materialRegistrationComponent);

        add(tabSheet);
    }
}
