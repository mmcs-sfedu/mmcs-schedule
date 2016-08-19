package com.nolan.mmcs_schedule.repository.primitives;

import java.util.ArrayList;
import java.util.TreeSet;

public class GroupLesson {
    public final LessonPeriod period;
    public final WeekType weekType;
    public final String subjectName;
    public final TreeSet<String> teachers;
    public final ArrayList<String> rooms;

    public GroupLesson(LessonPeriod period, WeekType weekType, String subjectName,
                       TreeSet<String> teachers, ArrayList<String> rooms) {
        this.period = period;
        this.weekType = weekType;
        this.subjectName = subjectName;
        this.teachers = teachers;
        this.rooms = rooms;
    }
}
