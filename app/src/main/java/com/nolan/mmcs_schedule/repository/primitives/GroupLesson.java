package com.nolan.mmcs_schedule.repository.primitives;

import java.util.ArrayList;
import java.util.TreeSet;

public class GroupLesson implements Comparable<GroupLesson> {
    private final LessonPeriod period;
    private final WeekType weekType;
    private final String subjectName;
    private final TreeSet<String> teachers;
    private final ArrayList<String> rooms;

    public GroupLesson(LessonPeriod period, WeekType weekType, String subjectName,
                       TreeSet<String> teachers, ArrayList<String> rooms) {
        this.period = period;
        this.weekType = weekType;
        this.subjectName = subjectName;
        this.teachers = teachers;
        this.rooms = rooms;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GroupLesson)) return false;
        GroupLesson another = (GroupLesson) obj;
        return period.equals(another.period) &&
                weekType.equals(another.weekType) &&
                subjectName.equals(another.subjectName) &&
                teachers.equals(another.teachers) &&
                rooms.equals(another.rooms);
    }

    @Override
    public int compareTo(GroupLesson groupLesson) {
        return period.getBegin().getHour() > groupLesson.period.getBegin().getHour() ? 1 : -1;
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

    public TreeSet<String> getTeachers() {
        return teachers;
    }

    public ArrayList<String> getRooms() {
        return rooms;
    }
}
