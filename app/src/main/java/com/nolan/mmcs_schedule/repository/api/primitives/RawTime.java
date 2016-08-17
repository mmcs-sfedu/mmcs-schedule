package com.nolan.mmcs_schedule.repository.api.primitives;

import java.io.Serializable;

public class RawTime
    implements Serializable {

    private int hours   = -1;
    // next field is allowed to be missed in response so we user 0 as initial value
    private int minutes = 0;

    public RawTime(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }
}
