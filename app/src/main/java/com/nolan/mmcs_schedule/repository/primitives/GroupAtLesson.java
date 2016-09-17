package com.nolan.mmcs_schedule.repository.primitives;

public class GroupAtLesson {
    private String name;
    private int gradeNum;
    private int groupNum;

    public GroupAtLesson(String name, int gradeNum, int groupNum) {
        this.name = name;
        this.gradeNum = gradeNum;
        this.groupNum = groupNum;
    }

    public String getName() {
        return name;
    }

    public int getGradeNum() {
        return gradeNum;
    }

    public int getGroupNum() {
        return groupNum;
    }
}
