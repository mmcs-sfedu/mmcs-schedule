package com.nolan.mmcs_schedule.repository.primitives;

public class HourMinute {
    private int hour;
    private int minute;

    public HourMinute(int hour, int minute) {
        assert 0 <= hour && hour < 24;
        assert 0 <= minute && minute < 60;
        this.hour = hour;
        this.minute = minute;
    }

    @Override
    public String toString() {
        return String.format("%1$02d:%2$02d", hour, minute);
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }
}
