package com.nolan.mmcs_schedule.repository.primitives;

public class LessonPeriod {
    public final HourMinute begin;
    public final HourMinute end;

    public LessonPeriod(HourMinute begin, HourMinute end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LessonPeriod)) return false;
        LessonPeriod another = (LessonPeriod) obj;
        return begin.equals(another.begin) &&
                end.equals(another.end);
    }
}
