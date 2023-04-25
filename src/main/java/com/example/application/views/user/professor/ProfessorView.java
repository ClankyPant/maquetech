package com.example.application.views.user.professor;

import com.example.application.components.maquetech.MaqueVerticalLayout;
import com.example.application.components.professor.ProfessorCodeGeneratorComponent;
import com.example.application.services.user.professor.ProfessorCodeService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed("ADMIN")
@PageTitle("Professores")
@Route(value = "professor", layout = MainLayout.class)
public class ProfessorView extends MaqueVerticalLayout {

    public ProfessorView(ProfessorCodeService professorCodeService) {
        setAlignItems(FlexComponent.Alignment.START);
        setJustifyContentMode(FlexComponent.JustifyContentMode.START);

        TabSheet tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        tabSheet.add("Gerador de código", new ProfessorCodeGeneratorComponent(professorCodeService));

        add(tabSheet);
    }
}
