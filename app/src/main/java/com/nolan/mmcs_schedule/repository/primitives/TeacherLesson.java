package com.nolan.mmcs_schedule.repository.primitives;

import java.util.ArrayList;

public class TeacherLesson implements Comparable<TeacherLesson>{
    private LessonPeriod period;
    private WeekType weekType;
    private String subjectName;
    private ArrayList<String> groups;
    private String room;

    public TeacherLesson(LessonPeriod period, WeekType weekType, String subjectName, ArrayList<String> groups,
                         String room) {
        this.period = period;
        this.weekType = weekType;
        this.subjectName = subjectName;
        this.groups = groups;
        this.room = room;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TeacherLesson)) return false;
        TeacherLesson another = (TeacherLesson) obj;
        return period.equals(another.period) &&
                weekType.equals(another.weekType) &&
                subjectName.equals(another.subjectName) &&
                groups.equals(another.groups) &&
                room.equals(another.room);
    }

    @Override
    public int compareTo(TeacherLesson teacherLesson) {
        return period.getBegin().getHour() > teacherLesson.period.getBegin().getHour() ? 1 : -1;
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
