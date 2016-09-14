package com.nolan.mmcs_schedule.repository.primitives;

import android.util.Pair;

import java.util.ArrayList;

public class LessonForTeacher {
    private LessonPeriod period;
    private WeekType weekType;
    private String subjectName;
    private ArrayList<String> groups;
    private String room;

    public LessonForTeacher(LessonPeriod period, WeekType weekType, String subjectName, ArrayList<String> groups,
                            String room) {
        this.period = period;
        this.weekType = weekType;
        this.subjectName = subjectName;
        this.groups = groups;
        this.room = room;
    }

    public LessonPeriod getPeriod() {
        return period;
    }

    public WeekType getWeekType() {
        return weekType;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public ArrayList<String> getGroups() {
        return groups;
    }

    public String getRoom() {
        return room;
    }
}
