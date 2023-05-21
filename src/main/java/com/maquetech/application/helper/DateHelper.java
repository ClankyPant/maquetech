package com.maquetech.application.helper;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateHelper {

    private static final SimpleDateFormat SDF_DATE = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat SDF_HOUR = new SimpleDateFormat("HH:mm");

    private DateHelper() {

    }

    public static Date parse(LocalDateTime localDateTime) {
        return Date.from(LocalDateTimeHelper.truncate(localDateTime).atZone(ZoneId.of("America/Sao_Paulo")).toInstant());
    }

    public static String displayDate(Date date) {
        return SDF_DATE.format(date);
    }

    public static  String displayHour(Date date) {
        return SDF_HOUR.format(date);
    }
}
