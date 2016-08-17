package com.nolan.mmcs_schedule.repository.api.primitives;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class RawGrade
    implements Serializable {

    public static class List extends ArrayList<RawGrade> { }

    public enum Degree {
        @SerializedName("bachelor")   BACHELOR,
        @SerializedName("master")     MASTER,
        @SerializedName("specialist") SPECIALIST;
    }

    private int    id     = -1;
    private int    num    = -1;
    private Degree degree = null;

    public RawGrade(int id, int num, Degree degree) {
        this.id = id;
        this.num = num;
        this.degree = degree;
    }

    public int getId() {
        return id;
    }

    public int getNum() {
        return num;
    }

    public Degree getDegree() {
        return degree;
    }
}
