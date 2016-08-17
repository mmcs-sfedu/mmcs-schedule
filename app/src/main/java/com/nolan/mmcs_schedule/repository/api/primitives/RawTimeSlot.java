package com.nolan.mmcs_schedule.repository.api.primitives;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class RawTimeSlot
    implements Serializable {

    public static class List extends ArrayList<RawTimeSlot> { }

    private                         int     id    = -1;
    private                         int     num   = -1;
    @SuppressWarnings("SpellCheckingInspection")
    @SerializedName("cbeg") private RawTime begin = null;
    @SuppressWarnings("SpellCheckingInspection")
    @SerializedName("cend") private RawTime end   = null;

    public RawTimeSlot(int id, int num, RawTime begin, RawTime end) {
        this.id = id;
        this.num = num;
        this.begin = begin;
        this.end = end;
    }

    public int getId() {
        return id;
    }

    public int getNum() {
        return num;
    }

    public RawTime getBegin() {
        return begin;
    }

    public RawTime getEnd() {
        return end;
    }
}
