package com.maquetech.application.components.professor;

import com.maquetech.application.components.maquetech.MaqueVerticalLayout;
import com.maquetech.application.services.user.professor.ProfessorCodeService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

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
