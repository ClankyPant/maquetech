package com.maquetech.application.helpers;

import com.maquetech.application.components.maquetech.notification.MaqueNotificationComponent;
import com.vaadin.flow.component.notification.NotificationVariant;

import java.util.concurrent.Callable;
import java.util.function.Function;

public class NotificationHelper {

    public static void error(String msg) {
        new MaqueNotificationComponent(msg, NotificationVariant.LUMO_ERROR);
    }

    public static void success(String msg) {
        new MaqueNotificationComponent(msg, NotificationVariant.LUMO_SUCCESS);
    }

    public static void runAndNotify(Runnable run, String successMessage) {
        try {
            run.run();
            success(successMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
            error(ex.getMessage());
        }
    }
}
