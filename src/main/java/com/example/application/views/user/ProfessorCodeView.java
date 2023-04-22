package com.example.application.views.user;

import com.example.application.components.maquetech.MaqueVerticalLayout;
import com.example.application.services.user.ProfessorCodeService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Locale;
import java.util.UUID;

@RolesAllowed("ADMIN")
@PageTitle("Gerarador código professor")
@Route(value = "gerador-codigo-professor", layout = MainLayout.class)
public class ProfessorCodeView extends MaqueVerticalLayout {

    private final ProfessorCodeService professorCodeService;

    public ProfessorCodeView(@Autowired ProfessorCodeService professorCodeService) {
        this.professorCodeService = professorCodeService;

        VerticalLayout vlContent = new VerticalLayout();
        vlContent.setSpacing(true);
        vlContent.setSizeUndefined();

        HorizontalLayout hlContent = new HorizontalLayout();
        hlContent.setAlignItems(Alignment.CENTER);

        TextField codeField = new TextField("Código");
        codeField.setHelperText("Salve o código após gerado!");
        codeField.setWidth("325px");
        codeField.setReadOnly(true);

        Button generateCodeButton = new Button("Gerar");
        generateCodeButton.addClickListener((event) -> {
            String professorCode = UUID.randomUUID().toString().toUpperCase();
            codeField.setValue(professorCode);
            this.professorCodeService.save(professorCode);
        });

        Button copyButton = new Button("Copiar");
        copyButton.addClickListener((event) -> {
            Page page = UI.getCurrent().getPage();
            page.executeJs("navigator.clipboard.writeText('" + codeField.getValue() + "');");
        });

        hlContent.add(generateCodeButton, copyButton);

        vlContent.add(codeField, hlContent);

        add(vlContent);
    }
}
