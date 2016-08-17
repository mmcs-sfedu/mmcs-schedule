package com.nolan.mmcs_schedule.repository.api.primitives;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("SpellCheckingInspection")
public class RawLesson
    implements Serializable {

    /**
     * Note:
     * timeSlot is lesson period in format: (%day_number%,%start_time%,%end_time%,%week_type%),<br>
     * where<br>
     * <i><b>day_number</b></i> is in (0, 6) and means the number of day of week,<br>
     * <i><b>start_time</b></i> and <i><b>end_time</b></i> are boundaries of lesson
     * in format HH:MM:SS<br>
     * <i><b>week_type</b></i> is either "upper" or "lower" or "full".
     */
    private                             int    id       = -1;
    @SerializedName("uberid") private   int    uberId   = -1;
    @SerializedName("subcount") private int    subCount = -1;
    @SerializedName("timeslot") private String timeSlot = null;

    public RawLesson(int id, int uberId, int subCount, String timeSlot) {
        this.id = id;
        this.uberId = uberId;
        this.subCount = subCount;
        this.timeSlot = timeSlot;
    }

    public int getId() {
        return id;
    }

    public int getUberId() {
        return uberId;
    }

    public int getSubCount() {
        return subCount;
    }

    public String getTimeSlot() {
        return timeSlot;
    }
}
