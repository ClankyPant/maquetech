package com.maquetech.application.components.search;

import com.maquetech.application.helpers.NotificationHelper;
import com.maquetech.application.listener.FilterSearchListener;
import com.maquetech.application.models.user.UserFilterModel;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class SearchComponent<T, D> extends VerticalLayout {

    protected final Binder<T> binder = new Binder<>();

    protected final List<D> dataList = new ArrayList<>();

    protected final List<FilterSearchListener> filterSearchListenerList = new ArrayList<>();

    public void addFilterSearchListener(FilterSearchListener filterSearchListener) {
        this.filterSearchListenerList.add(filterSearchListener);
    }

    public void afterSearch() {
        for (var filterSearchListener : this.filterSearchListenerList) {
            filterSearchListener.afterSearch();
        }
    }

    protected Component getComponent(Component... components) {
        throw new NotImplementedException();
    }

    protected void updateFilter(T filter) {
        try {
            binder.writeBean(filter);
        } catch (Exception ex) {
            ex.printStackTrace();
            NotificationHelper.error(ex.getMessage());
        }
    }
}