package utils;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Locale;
import java.util.Optional;
import java.time.format.DateTimeFormatter;

public final class Utils {
    private Utils() {}

    public static java.util.Date sqlDateToDate(final java.sql.Date sqlDate) {
        return sqlDate == null ? null : new java.util.Date(sqlDate.getTime());
    }

    public static java.sql.Date dateToSqlDate(final java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }

    public static Optional<java.util.Date> buildDate(final int day, final int month, final int year) {
        try {
            final String dateFormatString = "dd/MM/yyyy";
            final String dateString = day + "/" + month + "/" + year;
            final java.util.Date date = new SimpleDateFormat(dateFormatString, Locale.ITALIAN).parse(dateString);
            return Optional.of(date);
        } catch (final ParseException e) {
            return Optional.empty();
        }
    }

    public static java.time.LocalTime sqlTimeToTime(final java.sql.Time sqlTime) {
        if (sqlTime != null) {
            long timeMillis = sqlTime.getTime();
            return LocalTime.ofNanoOfDay(timeMillis * 1_000_000);
        }
        return null;
    }

    public static java.sql.Time timeToSqlTime(final java.time.LocalTime time) {
        if (time != null) {
            long timeMillis = time.toNanoOfDay() / 1_000_000;
            return new Time(timeMillis);
        }
        return null;
    }

    public String toString(LocalTime time) {
        return time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public LocalTime fromString(String string) {
        return LocalTime.parse(string, DateTimeFormatter.ofPattern("HH:mm"));
    }

}
