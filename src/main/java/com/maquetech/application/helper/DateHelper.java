package com.maquetech.application.helper;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class DateHelper {

    private DateHelper() {

    }

    public static Date parse(LocalDateTime localDateTime) {
        return Date.from(LocalDateTimeHelper.truncate(localDateTime).atZone(ZoneOffset.UTC).toInstant());
    }
}
