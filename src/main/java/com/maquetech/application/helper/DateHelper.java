package com.maquetech.application.helper;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateHelper {

    private DateHelper() {

    }

    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(truncate(localDateTime).atZone(ZoneOffset.UTC).toInstant());
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return truncate(date.toInstant().atZone(ZoneOffset.UTC).toLocalDateTime());
    }

    public static LocalDateTime truncate(LocalDateTime localDateTime) {
        return localDateTime.truncatedTo(ChronoUnit.MINUTES);
    }
}
