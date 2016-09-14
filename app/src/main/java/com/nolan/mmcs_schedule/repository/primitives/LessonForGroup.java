package com.nolan.mmcs_schedule.repository.primitives;

import android.util.Pair;

import java.util.ArrayList;

public class LessonForGroup {
    private LessonPeriod period;
    private WeekType weekType;
    private String subjectName;
    private ArrayList<Pair<String, String>> roomsAndTeachers;

    public LessonForGroup(LessonPeriod period, WeekType weekType, String subjectName,
                          ArrayList<Pair<String, String>> roomsAndTeachers) {
        this.period = period;
        this.weekType = weekType;
        this.subjectName = subjectName;
        this.roomsAndTeachers = roomsAndTeachers;
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

    public ArrayList<Pair<String, String>> getRoomsAndTeachers() {
        return roomsAndTeachers;
    }
}

