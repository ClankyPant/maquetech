package com.maquetech.application.helpers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class LocalDateTimeHelper {

    private LocalDateTimeHelper() {

    }

    public static LocalDateTime getTodayStartHour() {
        return LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
    }

    public static LocalDateTime getTodayFinalHour() {
        return LocalDateTime.now().withHour(23).withMinute(50).withSecond(0);
    }

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    public static LocalDateTime getNowPlus15Minutes() {
        var now = LocalDateTime.now();

        while ((now.getMinute() % 15) != 0) {
            now = now.plusMinutes(1);
        }

        return now;
    }

    public static LocalDateTime getNowPlus1HourAnd15Minutes() {
        return getNowPlus15Minutes().plusHours(1);
    }

    public static LocalDateTime parse(Date date) {
        return truncate(date.toInstant().atZone(ZoneId.of("America/Sao_Paulo")).toLocalDateTime());
    }

    public static LocalDateTime truncate(LocalDateTime localDateTime) {
        return localDateTime.truncatedTo(ChronoUnit.MINUTES);
    }

    public static LocalDateTime getNowPlusTwoWeek() {
        return getNowPlus15Minutes().plusWeeks(2);
    }
}
