package com.nolan.mmcs_schedule.repository.primitives;

import com.nolan.mmcs_schedule.repository.api.primitives.RawTeacher;

import java.util.ArrayList;

public class Teacher {
    public final int id;
    public final String name;

    public Teacher(RawTeacher rawTeacher) {
        id = rawTeacher.getId();
        name = rawTeacher.getName();
    }

    public static class List extends ArrayList<Teacher> {
        public List(ArrayList<RawTeacher> rawTeachers) {
            ensureCapacity(rawTeachers.size());
            for (RawTeacher rawTeacher : rawTeachers) {
                add(new Teacher(rawTeacher));
            }
        }
    }
}
