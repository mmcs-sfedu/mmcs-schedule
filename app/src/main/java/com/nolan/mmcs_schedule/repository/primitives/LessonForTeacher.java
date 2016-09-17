package com.nolan.mmcs_schedule.repository.primitives;

import java.util.ArrayList;

public class LessonForTeacher {
    private LessonPeriod period;
    private WeekType weekType;
    private String subjectName;
    private int dayOfWeek;
    private ArrayList<GroupAtLesson> groups;
    private String room;

    public LessonForTeacher(LessonPeriod period, WeekType weekType, String subjectName,
                            int dayOfWeek, ArrayList<GroupAtLesson> groups, String room) {
        this.period = period;
        this.weekType = weekType;
        this.subjectName = subjectName;
        this.dayOfWeek = dayOfWeek;
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

    public ArrayList<GroupAtLesson> getGroups() {
        return groups;
    }

    public String getRoom() {
        return room;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }
}
