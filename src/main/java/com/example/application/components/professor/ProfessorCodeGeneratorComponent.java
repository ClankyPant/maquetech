package com.example.application.components.professor;

import com.example.application.components.maquetech.MaqueVerticalLayout;
import com.example.application.services.user.professor.ProfessorCodeService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class ProfessorCodeGeneratorComponent extends MaqueVerticalLayout {

    public ProfessorCodeGeneratorComponent(ProfessorCodeService professorCodeService) {
        VerticalLayout vlContent = new VerticalLayout();
        vlContent.setSpacing(true);
        vlContent.setSizeUndefined();

        TextField codeField = new TextField("Código");
        codeField.setHelperText("Salve o código após gerado!");
        codeField.setWidth("325px");
        codeField.setReadOnly(true);

        Button generateCodeButton = new Button("Gerar");
        generateCodeButton.addClickListener((event) -> {
            String professorCode = UUID.randomUUID().toString().toUpperCase();
            codeField.setValue(professorCode);
            professorCodeService.save(professorCode);
        });

        vlContent.add(codeField, generateCodeButton);

        add(vlContent);
    }
}
