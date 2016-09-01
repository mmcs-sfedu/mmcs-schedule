package com.nolan.mmcs_schedule.repository.primitives;

import com.nolan.mmcs_schedule.repository.api.primitives.RawTeacher;

import java.io.Serializable;
import java.util.ArrayList;

public class Teacher {
    private int id;
    private String name;

    public Teacher(RawTeacher rawTeacher) {
        id = rawTeacher.getId();
        name = rawTeacher.getName();
    }

    public static class List extends ArrayList<Teacher> { }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
