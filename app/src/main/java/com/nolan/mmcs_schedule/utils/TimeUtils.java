package com.nolan.mmcs_schedule.utils;

/**
 * If you blame me for using this instead of SimpleDateFormat or at least
 * String Array Resource you probably better programmer than me.
 */
public class TimeUtils {
    private static String[] DAYS_OF_WEEK = {
            "Понедельник",
            "Вторник",
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
