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
        this.name = group.getName();
        this.num = group.getNum();
        this.gradeNum = gradeNum;
    }

    @Override
    public String toString() {
        if ("NULL".equals(name)) {
            return num + " группа";
        } else {
            return name + " " + num + " группа";
        }
    }

    public static class List extends ArrayList<Group> {
        public List(ArrayList<RawGroup> rawGroups, int gradeNum) {
            ensureCapacity(rawGroups.size());
            for (RawGroup rawGroup : rawGroups) {
                add(new Group(rawGroup, gradeNum));
            }
        }
    }
}
