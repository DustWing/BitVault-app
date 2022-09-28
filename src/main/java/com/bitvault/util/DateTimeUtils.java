package com.bitvault.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class DateTimeUtils {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");


    public static LocalDateTime parse(String value) {
        return value != null
                ? LocalDateTime.parse(value, DATE_TIME_FORMATTER) : null;
    }

    public static String format(TemporalAccessor temporalAccessor) {
        return temporalAccessor != null
                ? DATE_TIME_FORMATTER.format(temporalAccessor) : null;
    }

}
