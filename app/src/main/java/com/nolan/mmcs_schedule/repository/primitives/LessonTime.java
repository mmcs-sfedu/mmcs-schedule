package com.nolan.mmcs_schedule.repository.primitives;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LessonTime {
    private LessonPeriod period;
    private int dayOfWeek;
    private WeekType weekType;

    public LessonTime(String timeSlot) {
        Pattern pattern = Pattern.compile(
                "\\(([0-6]),(\\d\\d):(\\d\\d):00,(\\d\\d):(\\d\\d):00,(upper|lower|full)\\)");
        Matcher matcher = pattern.matcher(timeSlot);
        if (!matcher.matches()) throw new Error("Wrong timeSlot format.");
        dayOfWeek = Integer.parseInt(matcher.group(1));
        period = new LessonPeriod(
                new HourMinute(Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3))),
                new HourMinute(Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(5)))
        );
        switch (matcher.group(6).charAt(0)) {
            case 'u': weekType = WeekType.UPPER; break;
            case 'l': weekType = WeekType.LOWER; break;
            case 'f': weekType = WeekType.FULL; break;
            default:
                throw new Error("unreachable statement");
        }
    }

    public LessonPeriod getPeriod() {
        return period;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public WeekType getWeekType() {
        return weekType;
    }
}
