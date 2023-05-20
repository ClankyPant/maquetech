package com.maquetech.application.helpers;

import com.maquetech.application.components.maquetech.notification.MaqueNotificationComponent;
import com.vaadin.flow.component.notification.NotificationVariant;

public class NotificationHelper {

    public static void error(String msg) {
        new MaqueNotificationComponent(msg, NotificationVariant.LUMO_ERROR);
    }

    public static void success(String msg) {
        new MaqueNotificationComponent(msg, NotificationVariant.LUMO_SUCCESS);
    }
}