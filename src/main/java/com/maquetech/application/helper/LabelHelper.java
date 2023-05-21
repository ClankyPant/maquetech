package com.maquetech.application.helper;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class LabelHelper {

    public static Component getCenteredLabel(String message) {
        var label = new Label(message);

        var vlContent = new VerticalLayout();
        vlContent.add(label);
        vlContent.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, label);
        return vlContent;
    }
}
