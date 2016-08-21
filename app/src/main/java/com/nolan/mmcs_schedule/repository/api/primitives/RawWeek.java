package com.nolan.mmcs_schedule.repository.api.primitives;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RawWeek
    implements Serializable {

    public enum WeekType {
        @SerializedName("-1")UNKNOWN,
        @SerializedName("0")UPPER,
        @SerializedName("1")LOWER
    }

    private WeekType type;

    public RawWeek(WeekType type) {
        this.type = type;
    }

    public WeekType getType() {
        return type;
    }
}
