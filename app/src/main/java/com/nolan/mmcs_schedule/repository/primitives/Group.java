package com.nolan.mmcs_schedule.repository.primitives;

import com.nolan.mmcs_schedule.repository.api.primitives.RawGroup;

import java.util.ArrayList;

public class Group {
    public final int id;
    public final String name;

    public Group(RawGroup group, int gradeNum) {
        this.id = group.getId();
        this.name = group.getName() + " " + gradeNum + "." + group.getNum();
    }

    @Override
    public String toString() {
        return name;
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
