package com.maquetech.application.components.maquetech.grid;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;

public class MaqueGrid<T> extends Grid<T> {

    public MaqueGrid() {
        addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COLUMN_BORDERS);
    }
}
