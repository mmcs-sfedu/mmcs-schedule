package com.nolan.mmcs_schedule.utils;

public class TimeUtils {
    private static String[] DAYS_OF_WEEK = {
            "Понедельник",
            "Среда",
            "Четверг",
            "Пятница",
            "Суббота",
            "Воскресенье",
    };

    public static String getDisplayDayOfWeek(int dayOfWeek) {
        return DAYS_OF_WEEK[dayOfWeek];
    }
}
