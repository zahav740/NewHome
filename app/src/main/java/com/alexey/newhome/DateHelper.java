package com.alexey.newhome;

import java.util.Calendar;
import java.util.Locale;

public class DateHelper {
    public static Calendar getCalendarInstance() {
        return Calendar.getInstance();
    }

    public static int getYear(Calendar calendar) {
        return calendar.get(Calendar.YEAR);
    }

    public static int getMonth(Calendar calendar) {
        return calendar.get(Calendar.MONTH);
    }

    public static int getDayOfMonth(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static String formatDate(int year, int month, int dayOfMonth) {
        return String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
    }
}

