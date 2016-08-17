package com.nolan.mmcs_schedule.repository.api.primitives;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class RawGroup
    implements Serializable {

    public static class List extends ArrayList<RawGroup> { }

    private                            int    id      = -1;
    private                            String name    = null;
    private                            int    num     = -1;
    @SuppressWarnings("SpellCheckingInspection")
    @SerializedName("gradeid") private int    gradeId = -1;

    public RawGroup(int id, String name, int num, int gradeId) {
        this.id = id;
        this.name = name;
        this.num = num;
        this.gradeId = gradeId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNum() {
        return num;
    }

    public int getGradeId() {
        return gradeId;
    }
}
