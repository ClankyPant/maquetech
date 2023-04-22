package com.example.application.helpers;

import com.example.application.components.maquetech.notification.MaqueNotificationComponent;

public class NotificationHelper {

    public static void message(String msg) {
        new MaqueNotificationComponent(msg, false);
    }

    public static void error(String msg) {
        new MaqueNotificationComponent(msg, true);
    }
}
