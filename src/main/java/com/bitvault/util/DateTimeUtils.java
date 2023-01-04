package com.bitvault.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class DateTimeUtils {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public static String getTimeNow() {
        return  DATE_TIME_FORMATTER.format(LocalDateTime.now());
    }

    public static LocalDateTime parse(String value) {
        return value != null
                ? LocalDateTime.parse(value, DATE_TIME_FORMATTER) : null;
    }

    public static String format(LocalDateTime localDateTime) {
        return localDateTime != null
                ? DATE_TIME_FORMATTER.format(localDateTime) : null;
    }

}
