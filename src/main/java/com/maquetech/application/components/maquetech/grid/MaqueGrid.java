package com.maquetech.application.components.maquetech.grid;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.function.ValueProvider;

public class MaqueGrid<T> extends Grid<T> {

    public MaqueGrid() {
        addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COLUMN_BORDERS);
        setColumnReorderingAllowed(true);
    }

    @Override
    public Column<T> addColumn(ValueProvider<T, ?> valueProvider) {
        var column = super.addColumn(valueProvider);
        column.setSortable(true);

        return column;
    }
}
