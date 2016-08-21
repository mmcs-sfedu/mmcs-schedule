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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof HourMinute)) return false;
        HourMinute another = (HourMinute) obj;
        return hour == another.hour && minute == another.minute;
    }

    @Override
    public String toString() {
        return String.format("%1$02d:%2$02d", hour, minute);
    }
}
