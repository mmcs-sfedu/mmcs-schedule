package com.nolan.mmcs_schedule.repository.primitives;

public class LessonPeriod {
    private HourMinute begin;
    private HourMinute end;

    public LessonPeriod(HourMinute begin, HourMinute end) {
        this.begin = begin;
        this.end = end;
    }

    public HourMinute getBegin() {
        return begin;
    }

    public HourMinute getEnd() {
        return end;
    }
}
