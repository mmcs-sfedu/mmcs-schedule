package com.nolan.mmcs_schedule.repository.primitives;

import com.nolan.mmcs_schedule.repository.api.primitives.RawWeek;

public enum WeekType {
    UPPER, LOWER, FULL;

    public static WeekType convert(RawWeek rawWeekType) {
        switch (rawWeekType.getType()) {
            case UNKNOWN: return FULL;
            case UPPER: return UPPER;
            case LOWER: return LOWER;
            default:
                throw new Error("unreachable statement");
        }
    }
}
