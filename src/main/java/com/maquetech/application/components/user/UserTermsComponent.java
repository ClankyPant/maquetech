package com.maquetech.application.components.user;

import com.maquetech.application.helpers.ConvertHelper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class UserTermsComponent extends VerticalLayout {

    private Checkbox check;
    private final Dialog dialog = new Dialog();

    public UserTermsComponent() {
        setSizeUndefined();

        init();
        initDialog();
    }

    private void init() {
        check = new Checkbox();

        var clickHere = new Label("Eu li e concordo com os termos de uso!");
        clickHere.getStyle().set("text-decoration", "underline");

        var hlClick = new HorizontalLayout();
        hlClick.setSizeUndefined();
        hlClick.add(clickHere);
        hlClick.addClickListener(event -> dialog.open());

        var hlContent = new HorizontalLayout();
        hlContent.setSizeFull();
        hlContent.add(check, hlClick, dialog);
        hlContent.setAlignItems(Alignment.CENTER);

        add(hlContent);
    }

    public void initDialog() {
        var message = """
                    Bem-vindo ao nosso cadastro de usuário! Antes de prosseguir, gostaríamos de informar sobre a Lei Geral de Proteção de Dados (LGPD) e como ela se aplica ao nosso processo de coleta e utilização de informações pessoais. A LGPD é uma legislação que visa proteger a privacidade e os dados dos cidadãos brasileiros, garantindo que suas informações sejam tratadas de maneira adequada e segura.\n
                    Ao realizar este cadastro, você estará consentindo com a coleta e o processamento de seus dados pessoais, conforme descrito em nossa política de privacidade.\n
                    Durante o cadastro, iremos solicitar alguns dados pessoais, tais como seu nome, endereço de e-mail e número de telefone. Essas informações são necessárias para que possamos realizar diversas ações, como trocar sua senha ou entrar em contato.
                """;

        dialog.add(new Label(message));
        dialog.getFooter().add(new Button("Fechar", event -> dialog.close()));
    }

    public boolean isAceepted() {
        return this.check.getValue();
    }
}
