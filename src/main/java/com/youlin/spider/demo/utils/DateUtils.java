package com.youlin.spider.demo.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {
    public static Date parseUpdateDate(String lastUpdateDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd更新 HH:mm:ss");
        LocalDate date = LocalDate.parse(LocalDate.now().getYear() + "/" + lastUpdateDate + " 00:00:00", dateTimeFormatter);
        if (date.toEpochDay() > LocalDate.now().toEpochDay()) {
            date = LocalDate.parse((LocalDate.now().getYear() - 1) + "/" + lastUpdateDate + " 00:00:00", dateTimeFormatter);
        }
        ZonedDateTime zonedDateTime = date.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }
}
