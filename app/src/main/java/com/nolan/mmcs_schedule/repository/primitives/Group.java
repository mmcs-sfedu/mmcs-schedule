package com.nolan.mmcs_schedule.repository.primitives;

import com.nolan.mmcs_schedule.repository.api.primitives.RawGroup;

import java.util.ArrayList;

public class Group {
    public final int id;
    public final String name;
    public final int num;
    public final int gradeNum;

    public Group(RawGroup group, int gradeNum) {
        this.id = group.getId();
        this.name = group.getName().equals("NULL") ? null : group.getName();
        this.num = group.getNum();
        this.gradeNum = gradeNum;
    }

    public static class List extends ArrayList<Group> { }
}
