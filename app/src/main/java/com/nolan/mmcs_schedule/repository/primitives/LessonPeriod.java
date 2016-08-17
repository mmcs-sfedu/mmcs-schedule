package com.nolan.mmcs_schedule.repository.primitives;

public class LessonPeriod {
    public final HourMinute begin;
    public final HourMinute end;

    public LessonPeriod(HourMinute begin, HourMinute end) {
        this.begin = begin;
        this.end = end;
    }
}
