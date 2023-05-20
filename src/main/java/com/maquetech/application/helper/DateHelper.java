package com.maquetech.application.helper;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {

    public static Date convertToDate(LocalDateTime localDateTime) {
        var date = Date.from(localDateTime.atZone(ZoneOffset.UTC).toInstant());

        var calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, 3);

        return calendar.getTime();
    }
}
