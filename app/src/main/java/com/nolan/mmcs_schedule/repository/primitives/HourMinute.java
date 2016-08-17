package com.nolan.mmcs_schedule.repository.primitives;

public class HourMinute {
    public final int hour;
    public final int minute;

    public HourMinute(int hour, int minute) {
        assert 0 <= hour && hour < 24;
        assert 0 <= minute && minute < 60;
        this.hour = hour;
        this.minute = minute;
    }
}
