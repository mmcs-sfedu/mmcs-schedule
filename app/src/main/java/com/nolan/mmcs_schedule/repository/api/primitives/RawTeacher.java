package com.nolan.mmcs_schedule.repository.api.primitives;

import java.io.Serializable;
import java.util.ArrayList;

public class RawTeacher
    implements Serializable {

    public static class List extends ArrayList<RawTeacher> { }

    private int    id     = -1;
    private String name   = null;
    private String degree = null;

    public RawTeacher(int id, String name, String degree) {
        this.id = id;
        this.name = name;
        this.degree = degree;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDegree() {
        return degree;
    }
}
