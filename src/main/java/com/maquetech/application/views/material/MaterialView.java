package com.maquetech.application.views.material;

import com.maquetech.application.components.maquetech.MaqueVerticalLayout;
import com.maquetech.application.components.material.consult.MaterialConsultComponent;
import com.maquetech.application.components.material.MaterialRegistrationComponent;
import com.maquetech.application.services.material.MaterialService;
import com.maquetech.application.services.user.UserService;
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

    private final TabSheet tabSheet = new TabSheet();

    private final MaterialRegistrationComponent materialRegistrationComponent;

    public MaterialView(MaterialService materialService, UserService userService) throws NotFoundException {
        setAlignItems(Alignment.START);
        setJustifyContentMode(JustifyContentMode.START);

        materialRegistrationComponent = new MaterialRegistrationComponent(materialService);

        var materialConsultComponent = new MaterialConsultComponent(materialService, userService, true);
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
