package com.bitvault.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public static String getTimeNow() {
        return DATE_TIME_FORMATTER.format(LocalDateTime.now());
    }

    public static LocalDateTime toUtc(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }

    public static LocalDateTime toLocal(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
    }

    private static LocalDateTime parse(String value) {
        return value != null
                ? LocalDateTime.parse(value, DATE_TIME_FORMATTER) : null;
    }

    public static LocalDateTime parseToUtc(String value) {
        LocalDateTime parsed = parse(value);
        if (parsed == null) {
            return null;
        }

        return toUtc(parsed);
    }

    public static LocalDateTime parseToLocal(String value) {

        LocalDateTime parsed = parse(value);
        if (parsed == null) {
            return null;
        }

        return toLocal(parsed);
    }

    private static String format(LocalDateTime localDateTime) {
        return localDateTime != null
                ? DATE_TIME_FORMATTER.format(localDateTime) : null;
    }

    /**
     * Change the zone and return a formatted string
     *
     * @param value used to convert to string
     * @return Readable format of LocalDateTime
     * @see DateTimeUtils#format(LocalDateTime)
     */
    public static String formatToUtc(LocalDateTime value) {
        if (value == null) {
            return null;
        }
        LocalDateTime localDateTime = toUtc(value);
        return format(localDateTime);
    }

    public static String formatToLocal(LocalDateTime value) {
        if (value == null) {
            return null;
        }
        LocalDateTime localDateTime = toLocal(value);
        return format(localDateTime);
    }

}
