package com.maquetech.application.components;

import com.maquetech.application.components.maquetech.MaqueVerticalLayout;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;

public abstract class AbstractRegistrationComponent<E> extends MaqueVerticalLayout {

    protected Binder<E> binder = new Binder<>();

    protected FormLayout formLayout = new FormLayout();

    protected VerticalLayout vlContent = new VerticalLayout();

    protected AbstractRegistrationComponent() {
        vlContent.setSizeFull();
        vlContent.setWidth("50%");
    }
}
