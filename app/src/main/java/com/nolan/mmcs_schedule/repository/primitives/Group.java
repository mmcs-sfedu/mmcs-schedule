package com.nolan.mmcs_schedule.repository.primitives;

import com.nolan.mmcs_schedule.repository.api.primitives.RawGroup;

import java.util.ArrayList;

public class Group {
    private int id;
    private String name;
    private int num;
    private int gradeNum;

    public Group(RawGroup group, int gradeNum) {
        this.id = group.getId();
        this.name = group.getName() == null || group.getName().equals("NULL") ? null : group.getName();
        this.num = group.getNum();
        this.gradeNum = gradeNum;
    }

    public static class List extends ArrayList<Group> { }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNum() {
        return num;
    }

    public int getGradeNum() {
        return gradeNum;
    }
}
